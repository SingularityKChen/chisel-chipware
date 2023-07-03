import chisel3._
import chisel3.util._
import chisel3.experimental._

/**
  * == CW_fifo_2c_df ==
  *
  * === Parameters ===
  *
  * | Parameter        | Legal Range  | Default      | Description    |
  * |------------------|--------------|--------------|----------------|
  * | width            | 2 to 1024    | default 8    | width of data_s/data_d |
  * | ram_depth        | 2 to 1024    | default 8    | depth of ram |
  * | mem_mode         | 0            |              | no retiming |
  * | f_sync_type      | 0 to 4       | 2 (default)  | 2-sample synchronizer |
  * | r_sync_type      | 0 to 4       | 2 (default)  | 2-sample synchronizer |
  * | clk_ratio        |              | 1 (default)  | This parameter is not used. It is a place holder for quantized (integer) ratio clk_s/clk_d. |
  * | rst_mode         | 0            | 0 (default)  | RAM is included is reset mechanism |
  * | err_mode         | 0            |              | This parameter is not used. It is a place holder for future sticky/dynamic behaviour of error flags |
  * | tst_mode         | 0            | 0 (default)  | no scan latch inserted |
  * | verif_en         | 0            | 0 (default)  | inserts 0 random sampling error |
  * | clr_dual_domain  | 0 or 1       | 1 (default)  | This parameter is not used. It is reserved for possible future use in the functionality of co-ordinated clars. Currently, both clr_s and clr_d can be used to simultaneously initiate a co-ordinated clear |
  * | arch_type        |              | 0 (default)  | This parameter is not used. It is simply a place holder |
  *
  * === Ports ===
  *
  * | Name            | Width        | Direction | Description |
  * |-----------------|--------------|-----------|-------------|
  * | rst_s_n         | 1 bit        | input     | source asynchronous reset |
  * | init_s_n        | 1 bit        | input     | source synchronous reset |
  * | clk_s           | 1 bit        | input     | source clock |
  * | push_s_n        | 1 bit        | input     | source (write) push requst |
  * | data_s          | width        | input     | source push data |
  * | rst_d_n         | 1 bit        | input     | destination asynchronous reset |
  * | init_d_n        | 1 bit        | input     | destination synchronous reset |
  * | clk_d           | 1 bit        | input     | destination clock |
  * | pop_d_n         | 1 bit        | input     | destination (read) pop request |
  * | clr_s           | 1 bit        | input     | source initiated clear ram |
  * | clr_d           | 1 bit        | input     | destination initiated clear ram |
  * | ae_level_s      | **1          | input     | source almost empty level |
  * | af_level_s      | **1          | input     | source almost full level |
  * | ae_level_d      | **2          | input     | destination fifo almost empty level |
  * | af_level_d      | **2          | input     | destination fifo almost full level |
  * | test            | 1 bit        | input     | scan test mode select used if parameter tst_mode is 1 or 2 |
  * | data_d          | width        | output    | destination pop data |
  * | clr_sync_s      | 1 bit        | output    | source initiated coordinated clear |
  * | clr_in_prog_s   | 1 bit        | output    | source initiated clear in progress |
  * | clr_cmplt_s     | 1 bit        | output    | source clear complete status pulse |
  * | clr_sync_d      | 1 bit        | output    | destination initiated coordinated clear |
  * | clr_in_prog_d   | 1 bit        | output    | destination initiated clear in progress |
  * | clr_cmplt_d     | 1 bit        | output    | destination clear complete status pulse |
  * | fifo_empty_s    | 1 bit        | output    | source fifo (ram + cache) empty flag |
  * | empty_s         | 1 bit        | output    | source ram empty flag |
  * | almost_empty_s  | 1 bit        | output    | source ram almost empty flag |
  * | half_full_s     | 1 bit        | output    | source ram half full flag |
  * | almost_full_s   | 1 bit        | output    | source ram almost full flag |
  * | full_s          | 1 bit        | output    | source ram full flag |
  * | error_s         | 1 bit        | output    | source push error flag |
  * | word_cnt_s      | **1         | output    | source ram word count |
  * | fifo_word_cnt_s | **2         | output    | source fifo word count |
  * | empty_d         | 1 bit        | output    | destination fifo empty flag |
  * | almost_empty_d  | 1 bit        | output    | destination fifo almost empty flag |
  * | half_full_d     | 1 bit        | output    | destination fifo half full flag |
  * | almost_full_d   | 1 bit        | output    | destination fifo almost full flag |
  * | full_d          | 1 bit        | output    | destination fifo full flag |
  * | error_d         | 1 bit        | output    | destination pop error flag |
  * | word_cnt_d      | **1         | output    | destination ram word count |
  *
  * **1 = ceil(log2(ram_depth+1))
  * **2 = ceil(log2(eff_depth+1))
  *
  * @param width width of data_s/data_d (2 to 1024)
  * @param ram_depth depth of ram (2 to 1024)
  * @param mem_mode no retiming
  * @param f_sync_type 2-sample synchronizer
  * @param r_sync_type 2-sample synchronizer
  * @param clk_ratio This parameter is not used. It is a place holder for quantized (integer) ratio clk_s/clk_d.
  * @param rst_mode RAM is included is reset mechanism
  * @param err_mode This parameter is not used. It is a place holder for future sticky/dynamic behaviour of error flags
  * @param tst_mode no scan latch inserted
  * @param verif_en inserts 0 random sampling error
  * @param clr_dual_domain This parameter is not used. It is reserved for possible future use in the functionality of co-ordinated clars. Currently, both clr_s and clr_d can be used to simultaneously initiate a co-ordinated clear
  * @param arch_type This parameter is not used. It is simply a place holder
  */
class CW_fifo_2c_df(
  val width:           Int = 8,
  val ram_depth:       Int = 8,
  val mem_mode:        Int = 0,
  val f_sync_type:     Int = 2,
  val r_sync_type:     Int = 2,
  val clk_ratio:       Int = 1,
  val rst_mode:        Int = 0,
  val err_mode:        Int = 0,
  val tst_mode:        Int = 0,
  val verif_en:        Int = 0,
  val clr_dual_domain: Int = 1,
  val arch_type:       Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "ram_depth" -> ram_depth,
        "mem_mode" -> mem_mode,
        "f_sync_type" -> f_sync_type,
        "r_sync_type" -> r_sync_type,
        "clk_ratio" -> clk_ratio,
        "rst_mode" -> rst_mode,
        "err_mode" -> err_mode,
        "tst_mode" -> tst_mode,
        "verif_en" -> verif_en,
        "clr_dual_domain" -> clr_dual_domain,
        "arch_type" -> arch_type
      )
    ) {
  require(width >= 2 && width <= 1024, s"width must be in range [2, 1024], but got $width")
  require(ram_depth >= 2 && ram_depth <= 1024, s"ram_depth must be in range [2, 1024], but got $ram_depth")
  require(mem_mode == 0, s"mem_mode must be 0, but got $mem_mode")
  require(f_sync_type >= 0 && f_sync_type <= 4, s"f_sync_type must be in range [0, 4], but got $f_sync_type")
  require(r_sync_type >= 0 && r_sync_type <= 4, s"r_sync_type must be in range [0, 4], but got $r_sync_type")
  require(rst_mode == 0, s"rst_mode must be 0, but got $rst_mode")
  require(tst_mode == 0, s"tst_mode must be 0, but got $tst_mode")
  require(verif_en == 0, s"verif_en must be 0, but got $verif_en")

  protected val eff_depth: Int = mem_mode match {
    case 0 => ram_depth + 1
    case 1 => ram_depth + 2
    case 2 => ram_depth + 2
    case 3 => ram_depth + 3
    case 4 => ram_depth + 1
    case 5 => ram_depth + 2
    case 6 => ram_depth + 2
    case _ => throw new IllegalArgumentException(s"Invalid mem_mode: $mem_mode")
  }
  protected val BWBOmaxFIFOwrdCnt:    Int     = log2Ceil(eff_depth + 1)
  protected val BWBOmaxRAMaddr:       Int     = log2Ceil(ram_depth)
  protected val BWBOmaxRAMwrdCnt:     Int     = log2Ceil(ram_depth + 1)
  protected val maxPtlRAMdepth_r_w_s: Int     = 1 << BWBOmaxRAMaddr
  protected val NLlessThanMPD_r_w_s:  Int     = maxPtlRAMdepth_r_w_s - ram_depth
  protected val isPWRof2_r_w_s:       Boolean = NLlessThanMPD_r_w_s == 0
  protected val isODD_r_w_s:          Boolean = (ram_depth % 2) == 1

  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_s_n:         Bool  = Input(Bool()) // source asynchronous reset
    val init_s_n:        Bool  = Input(Bool()) // source synchronous reset
    val clk_s:           Clock = Input(Clock()) // source clock
    val push_s_n:        Bool  = Input(Bool()) // source (write) push requst
    val rst_d_n:         Bool  = Input(Bool()) // destination asynchronous reset
    val init_d_n:        Bool  = Input(Bool()) // destination synchronous reset
    val clk_d:           Clock = Input(Clock()) // destination clock
    val pop_d_n:         Bool  = Input(Bool()) // destination (read) pop request
    val data_s:          UInt  = Input(UInt(width.W)) // source push data
    val clr_s:           Bool  = Input(Bool()) // source initiated clear ram
    val clr_d:           Bool  = Input(Bool()) // destination initiated clear ram
    val ae_level_s:      UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W)) // source almost empty level
    val af_level_s:      UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W)) // source almost full level
    val ae_level_d:      UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W)) // destination fifo almost empty level
    val af_level_d:      UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W)) // destination fifo almost full level
    val test:            Bool  = Input(Bool()) // scan test mode select used if parameter tst_mode is 1 or 2
    val data_d:          UInt  = Output(UInt(width.W)) // destination pop data
    val clr_sync_s:      Bool  = Output(Bool()) // source initiated coordinated clear
    val clr_in_prog_s:   Bool  = Output(Bool()) // source initiated clear in progress
    val clr_cmplt_s:     Bool  = Output(Bool()) // source clear complete status pulse
    val clr_sync_d:      Bool  = Output(Bool()) // destination initiated coordinated clear
    val clr_in_prog_d:   Bool  = Output(Bool()) // destination initiated clear in progress
    val clr_cmplt_d:     Bool  = Output(Bool()) // destination clear complete status pulse
    val fifo_empty_s:    Bool  = Output(Bool()) // source fifo (ram + cache) empty flag
    val empty_s:         Bool  = Output(Bool()) // source ram empty flag
    val almost_empty_s:  Bool  = Output(Bool()) // source ram almost empty flag
    val half_full_s:     Bool  = Output(Bool()) // source ram half full flag
    val almost_full_s:   Bool  = Output(Bool()) // source ram almost full flag
    val full_s:          Bool  = Output(Bool()) // source ram full flag
    val error_s:         Bool  = Output(Bool()) // source push error flag
    val word_cnt_s:      UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W)) // source ram word count
    val fifo_word_cnt_s: UInt  = Output(UInt(BWBOmaxFIFOwrdCnt.W)) // source fifo word count
    val empty_d:         Bool  = Output(Bool()) // destination fifo empty flag
    val almost_empty_d:  Bool  = Output(Bool()) // destination fifo almost empty flag
    val half_full_d:     Bool  = Output(Bool()) // destination fifo half full flag
    val almost_full_d:   Bool  = Output(Bool()) // destination fifo almost full flag
    val full_d:          Bool  = Output(Bool()) // destination fifo full flag
    val error_d:         Bool  = Output(Bool()) // destination pop error flag
    val word_cnt_d:      UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W)) // destination ram word count
  })
}
