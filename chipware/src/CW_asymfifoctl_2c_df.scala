// filename: CW_asymfifoctl_2c_df.scala
import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_asymfifoctl_2c_df ==
  *
  * === Abstract ===
  *
  * Asymmetric Dual Clock FIFO Controller with Dynamic Flags
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | data_s_width  | 1 to 2<sup>10</sup> | 16 | width of data_s |
  * | data_d_width  | 1 to 2<sup>10</sup> | 8 | width of data_d |
  * | ram_depth  | 2 to 2<sup>24</sup> | 8 | depth of ram |
  * | mem_mode  | 0 to 7 | 3 | RAM data out and read address re-timing |
  * | arch_type  | 1 | 1 | This parameter is not used. It is simply a place holder |
  * | f_sync_type  | 0 to 4 | 2 | 2-sample synchronizer with both sample pos-edge |
  * | r_sync_type  | 0 to 4 | 2 | 2-sample synchronizer with both sample pos-edge |
  * | byte_order  | 0 to 1 | 0 | 1st sub-word read/written is msb-subword |
  * | flush_value  | 0 to 1 | 0 | unassigned sub-words flushed with zeros |
  * | clk_ratio  | Any | 1 | This parameter is not used. It is a place holder for quantized (integer) ratio clk_s/clk_d. |
  * | ram_re_ext  | 0 to 1 | 0 | This parameter is not used. It is a place holder for ram_re_d_n behaviour. Currently ram_re_d_n is a single active high pulse. |
  * | err_mode  | 0 to 1 | 0 | This parameter is not used. It is a place holder for future sticky/dynamic behaviour of error flags |
  * | tst_mode  | 0 to 4 | 0 | no scan latch inserted |
  * | verif_en  | 0 to 4 | 0 | inserts 0 random sampling error |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | rst_s_n | 1 bit | Input | source asynchronous reset |
  * | init_s_n | 1 bit | Input | source synchronous reset |
  * | clk_s | 1 bit | Input | source clock |
  * | push_s_n | 1 bit | Input | source (write) push requst |
  * | flush_s_n | 1 bit | Input | source flush request (data_s_width < data_d_width) |
  * | data_s | data_s_width | Input | source input sub-word (data_s_width < data_d_width) |
  * | rst_d_n | 1 bit | Input | destination asynchronous reset |
  * | init_d_n | 1 bit | Input | destination synchronous reset |
  * | clk_d | 1 bit | Input | destination clock |
  * | pop_d_n | 1 bit | Input | destination (read) pop request |
  * | data_d | data_d_width | Output | destination read data |
  * | wr_en_s_n | 1 bit | Output | source write enable to ram |
  * | wr_addr_s | ceil(log2(ram_depth)) | Output | source write address to ram |
  * | wr_data_s | width | Output | source write data to ram |
  * | ram_re_d_n | 1 bit | Output | destination read enable to ram |
  * | rd_addr_d | ceil(log2(ram_depth)) | Output | destination read address to ram |
  * | rd_data_d | width | Input | destination data from ram |
  * | clr_s | 1 bit | Input | source synchronous reset |
  * | clr_sync_s | 1 bit | Output | source initiated coordinated clear |
  * | clr_in_prog_s | 1 bit | Output | source initiated clear in progress |
  * | clr_cmplt_s | 1 bit | Output | source clear complete status pulse |
  * | clr_d | 1 bit | Input | destination synchronous reset |
  * | clr_sync_d | 1 bit | Output | destination initiated coordinated clear |
  * | clr_in_prog_d | 1 bit | Output | destination initiated clear in progress |
  * | clr_cmplt_d | 1 bit | Output | destination clear complete status pulse |
  * | ae_level_s | ceil(log2(ram_depth+1)) | Input | source almost empty level |
  * | af_level_s | ceil(log2(ram_depth+1)) | Input | source almost full level |
  * | fifo_empty_s | 1 bit | Output | source fifo (ram + cache) empty flag |
  * | empty_s | 1 bit | Output | source ram empty flag |
  * | almost_empty_s | 1 bit | Output | source ram almost empty flag |
  * | half_full_s | 1 bit | Output | source ram half full flag |
  * | almost_full_s | 1 bit | Output | source ram almost full flag |
  * | full_s | 1 bit | Output | source ram full flag |
  * | inbuf_full_s | 1 bit | Output | source input buffer full (data_s_width < data_d_width) |
  * | inbuf_part_wd_s | 1 bit | Output | source partial word pushed (data_s_width < data_d_width) |
  * | push_error_s | 1 bit | Output | source push error flag |
  * | ram_word_cnt_s | ceil(log2(ram_depth+1)) | Output | source ram word count |
  * | fifo_word_cnt_s | ceil(log2(eff_depth+1)) | Output | source fifo word count |
  * | ae_level_d | ceil(log2(eff_depth+1)) | Input | destination almost empty level |
  * | af_level_d | ceil(log2(eff_depth+1)) | Input | destination almost full level |
  * | empty_d | 1 bit | Output | destination fifo empty flag |
  * | almost_empty_d | 1 bit | Output | destination fifo almost empty flag |
  * | half_full_d | 1 bit | Output | destination fifo half full flag |
  * | almost_full_d | 1 bit | Output | destination fifo almost full flag |
  * | full_d | 1 bit | Output | destination fifo full flag |
  * | pop_error_d | 1 bit | Output | destination pop error flag |
  * | outbuf_part_wd_d | 1 bit | Output | destination partial word popped (data_s_width > data_d_width) |
  * | ram_word_cnt_d | ceil(log2(ram_depth+1)) | Output | destination ram word count |
  * | fifo_word_cnt_d | ceil(log2(eff_depth+1)) | Output | destination fifo word count |
  * | test | 1 bit | Input | scan test mode select |
  *
  * @param data_s_width width of data_s
  * @param data_d_width width of data_d
  * @param ram_depth depth of ram
  * @param mem_mode RAM data out and read address re-timing.
  *                 | 0	| no retiming
  *                 | 1	| RAM data out (post) re-timing
  *                 | 2	| RAM read address (pre) re-timing
  *                 | 3 (default) | RAM data out and read address re-timing
  *                 | 4	| RAM write interface (pre) re-timing
  *                 | 5	| RAM write interface and RAM data out re-timing
  *                 | 6	| RAM write interface and read address re-timing
  *                 | 7	| RAM data out, write interface and read address
  * @param arch_type This parameter is not used. It is simply a place holder
  * @param f_sync_type 2-sample synchronizer with both sample pos-edge
  * @param r_sync_type 2-sample synchronizer with both sample pos-edge
  * @param byte_order 1st sub-word read/written is msb-subword
  * @param flush_value unassigned sub-words flushed with zeros
  * @param clk_ratio This parameter is not used. It is a place holder for quantized (integer) ratio clk_s/clk_d.
  * @param ram_re_ext This parameter is not used. It is a place holder for ram_re_d_n behaviour. Currently ram_re_d_n is a single active high pulse.
  * @param err_mode This parameter is not used. It is a place holder for future sticky/dynamic behaviour of error flags
  * @param tst_mode no scan latch inserted
  * @param verif_en inserts 0 random sampling error
  */
class CW_asymfifoctl_2c_df(
  val data_s_width: Int = 16,
  val data_d_width: Int = 8,
  val ram_depth:    Int = 8,
  val mem_mode:     Int = 3,
  val arch_type:    Int = 1,
  val f_sync_type:  Int = 2,
  val r_sync_type:  Int = 2,
  val byte_order:   Int = 0,
  val flush_value:  Int = 0,
  val clk_ratio:    Int = 1,
  val ram_re_ext:   Int = 0,
  val err_mode:     Int = 0,
  val tst_mode:     Int = 0,
  val verif_en:     Int = 0)
    extends BlackBox(
      Map(
        "data_s_width" -> data_s_width,
        "data_d_width" -> data_d_width,
        "ram_depth" -> ram_depth,
        "mem_mode" -> mem_mode,
        "arch_type" -> arch_type,
        "f_sync_type" -> f_sync_type,
        "r_sync_type" -> r_sync_type,
        "byte_order" -> byte_order,
        "flush_value" -> flush_value,
        "clk_ratio" -> clk_ratio,
        "ram_re_ext" -> ram_re_ext,
        "err_mode" -> err_mode,
        "tst_mode" -> tst_mode,
        "verif_en" -> verif_en
      )
    )
    with HasBlackBoxPath {
  require(data_s_width >= 1 && data_s_width <= 1024, "data_s_width should be in range [1, 1024]")
  require(data_d_width >= 1 && data_d_width <= 1024, "data_d_width should be in range [1, 1024]")
  require(ram_depth >= 2 && ram_depth <= 16777216, "ram_depth should be in range [2, 16777216]")
  require(mem_mode >= 0 && mem_mode <= 7, "mem_mode should be in range [0, 7]")
  require(arch_type == 1, "arch_type should be 1")
  require(f_sync_type >= 0 && f_sync_type <= 4, "f_sync_type should be in range [0, 4]")
  require(r_sync_type >= 0 && r_sync_type <= 4, "r_sync_type should be in range [0, 4]")
  require(byte_order >= 0 && byte_order <= 1, "byte_order should be in range [0, 1]")
  require(flush_value >= 0 && flush_value <= 1, "flush_value should be in range [0, 1]")
  require(clk_ratio == 1, "clk_ratio must be 1")
  require(ram_re_ext >= 0 && ram_re_ext <= 1, "ram_re_ext should be in range [0, 1]")
  require(err_mode >= 0 && err_mode <= 1, "err_mode should be in range [0, 1]")
  require(tst_mode >= 0 && tst_mode <= 2, "tst_mode should be in range [0, 2]")
  require(verif_en >= 0 && verif_en <= 4, "verif_en should be in range [0, 4]")

  protected val eff_depth: Int = mem_mode match {
    case 0 => ram_depth + 1
    case 1 => ram_depth + 2
    case 2 => ram_depth + 2
    case 3 => ram_depth + 3
    case 4 => ram_depth + 1
    case 5 => ram_depth + 2
    case 6 => ram_depth + 2
    case _ => ram_depth + 3
  }
  // bus size based on the Maximum FIFO Word Count
  protected val BWBOmaxFIFOwrdCnt: Int = log2Ceil(eff_depth + 1)
  // bus size based on Maximum Ram Address
  protected val BWBOmaxRAMaddr: Int = log2Ceil(ram_depth)
  // bus size based on the Maximum RAM Word Count
  protected val BWBOmaxRAMwrdCnt: Int = log2Ceil(ram_depth + 1)
  // asymmetric side is the data interface of lesser width
  protected val width: Int = if (data_s_width > data_d_width) data_s_width else data_d_width

  val io = IO(new Bundle {
    val rst_s_n:          Bool  = Input(Bool())
    val init_s_n:         Bool  = Input(Bool())
    val clk_s:            Clock = Input(Clock())
    val push_s_n:         Bool  = Input(Bool())
    val flush_s_n:        Bool  = Input(Bool())
    val data_s:           UInt  = Input(UInt(data_s_width.W))
    val rst_d_n:          Bool  = Input(Bool())
    val init_d_n:         Bool  = Input(Bool())
    val clk_d:            Clock = Input(Clock())
    val pop_d_n:          Bool  = Input(Bool())
    val data_d:           UInt  = Output(UInt(data_d_width.W))
    val wr_en_s_n:        Bool  = Output(Bool())
    val wr_addr_s:        UInt  = Output(UInt(BWBOmaxRAMaddr.W))
    val wr_data_s:        UInt  = Output(UInt(width.W))
    val ram_re_d_n:       Bool  = Output(Bool())
    val rd_addr_d:        UInt  = Output(UInt(BWBOmaxRAMaddr.W))
    val rd_data_d:        UInt  = Input(UInt(width.W))
    val clr_s:            Bool  = Input(Bool())
    val clr_sync_s:       Bool  = Output(Bool())
    val clr_in_prog_s:    Bool  = Output(Bool())
    val clr_cmplt_s:      Bool  = Output(Bool())
    val clr_d:            Bool  = Input(Bool())
    val clr_sync_d:       Bool  = Output(Bool())
    val clr_in_prog_d:    Bool  = Output(Bool())
    val clr_cmplt_d:      Bool  = Output(Bool())
    val ae_level_s:       UInt  = Input(UInt(BWBOmaxRAMwrdCnt.W))
    val af_level_s:       UInt  = Input(UInt(BWBOmaxRAMwrdCnt.W))
    val fifo_empty_s:     Bool  = Output(Bool())
    val empty_s:          Bool  = Output(Bool())
    val almost_empty_s:   Bool  = Output(Bool())
    val half_full_s:      Bool  = Output(Bool())
    val almost_full_s:    Bool  = Output(Bool())
    val full_s:           Bool  = Output(Bool())
    val inbuf_full_s:     Bool  = Output(Bool())
    val inbuf_part_wd_s:  UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W))
    val push_error_s:     Bool  = Output(Bool())
    val ram_word_cnt_s:   UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W))
    val fifo_word_cnt_s:  UInt  = Output(UInt(BWBOmaxFIFOwrdCnt.W))
    val ae_level_d:       UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W))
    val af_level_d:       UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W))
    val empty_d:          Bool  = Output(Bool())
    val almost_empty_d:   Bool  = Output(Bool())
    val half_full_d:      Bool  = Output(Bool())
    val almost_full_d:    Bool  = Output(Bool())
    val full_d:           Bool  = Output(Bool())
    val pop_error_d:      Bool  = Output(Bool())
    val outbuf_part_wd_d: UInt  = Output(UInt(BWBOmaxFIFOwrdCnt.W))
    val ram_word_cnt_d:   UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W))
    val fifo_word_cnt_d:  UInt  = Output(UInt(BWBOmaxFIFOwrdCnt.W))
    val test:             Bool  = Input(Bool())
  })
}
