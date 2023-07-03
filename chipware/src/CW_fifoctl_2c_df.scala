import chisel3._
import chisel3.util._
import chisel3.experimental._

/**
  * == CW_fifoctl_2c_df ==
  *
  * === Abstract ===
  *
  * Dual Clock FIFO Controller with Dynamic Flags
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Range | Default | Description                                      |
  * |--------------|-------------|---------|--------------------------------------------------|
  * | width        | 2 to 2<sup>10</sup>   | 8       | width of data_s/data_d                           |
  * | ram_depth    | 2 to 2<sup>10</sup>   | 8       | depth of ram                                     |
  * | mem_mode     | 0           | 0       | no retiming                                      |
  * | f_sync_type  | 0 to 4      | 2       | synchronization mechanism for source clock       |
  * | r_sync_type  | 0 to 4      | 2       | synchronization mechanism for destination clock  |
  * | clk_ratio    | 1           | 1       | not used, placeholder for clk_s/clk_d ratio      |
  * | ram_re_ext   | 0           | 0       | not used, placeholder for ram_re_d_n behavior    |
  * | err_mode     | 0 to 1      | 0       | not used, placeholder for error flag behavior    |
  * | tst_mode     | 0           | 0       | no scan latch inserted                           |
  * | verif_en     | 0           | 0       | inserts 0 random sampling error                  |
  * | clr_dual_domain | 0 to 1   | 1       | not used, reserved for future use                |
  * | arch_type    | 0           | 0       | not used, placeholder                            |
  *
  * === Ports ===
  *
  * | Name          | Width                 | Direction | Description                            |
  * |---------------|-----------------------|-----------|----------------------------------------|
  * | rst_s_n       | 1 bit                 | Input     | source asynchronous reset              |
  * | init_s_n      | 1 bit                 | Input     | source synchronous reset               |
  * | clk_s         | 1 bit                 | Input     | source clock                           |
  * | push_s_n      | 1 bit                 | Input     | source (write) push request            |
  * | rst_d_n       | 1 bit                 | Input     | destination asynchronous reset         |
  * | init_d_n      | 1 bit                 | Input     | destination synchronous reset          |
  * | clk_d         | 1 bit                 | Input     | destination clock                      |
  * | pop_d_n       | 1 bit                 | Input     | destination (read) pop request         |
  * | data_d        | width                 | Output    | destination pop data                   |
  * | wr_en_s_n     | 1 bit                 | Output    | source write enable to ram             |
  * | wr_addr_s     | ceil(log2(ram_depth)) | Output    | source write address to ram            |
  * | ram_re_d_n    | 1 bit                 | Output    | destination read enable to ram         |
  * | rd_addr_d     | ceil(log2(ram_depth)) | Output    | destination read address to ram        |
  * | rd_data_d     | width                 | Input     | destination data from fifo             |
  * | clr_s         | 1 bit                 | Input     | source initiated clear ram             |
  * | clr_sync_s    | 1 bit                 | Output    | source coordinated clear               |
  * | clr_in_prog_s | 1 bit                 | Output    | source clear in progress               |
  * | clr_cmplt_s   | 1 bit                 | Output    | source clear complete status pulse     |
  * | clr_d         | 1 bit                 | Input     | destination initiated clear ram        |
  * | clr_sync_d    | 1 bit                 | Output    | destination coordinated clear          |
  * | clr_in_prog_d | 1 bit                 | Output    | destination clear in progress          |
  * | clr_cmplt_d   | 1 bit                 | Output    | destination clear complete status pulse|
  *
  * @param width Width of data_s/data_d (2 to 2<sup>10</sup>)
  * @param ram_depth Depth of ram (2 to 2<sup>10</sup>)
  * @param mem_mode Memory mode (0: no retiming)
  * @param f_sync_type Synchronization mechanism for source clock (0 to 4)
  * @param r_sync_type Synchronization mechanism for destination clock (0 to 4)
  * @param clk_ratio Not used, placeholder for clk_s/clk_d ratio (1)
  * @param ram_re_ext Not used, placeholder for ram_re_d_n behavior (0)
  * @param err_mode Not used, placeholder for error flag behavior (0 to 1)
  * @param tst_mode No scan latch inserted (0)
  * @param verif_en Inserts 0 random sampling error (0)
  * @param clr_dual_domain Not used, reserved for future use (0 to 1)
  * @param arch_type Not used, placeholder (0)
  */
class CW_fifoctl_2c_df(
  val width:           Int = 8,
  val ram_depth:       Int = 8,
  val mem_mode:        Int = 0,
  val f_sync_type:     Int = 2,
  val r_sync_type:     Int = 2,
  val clk_ratio:       Int = 1,
  val ram_re_ext:      Int = 0,
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
        "ram_re_ext" -> ram_re_ext,
        "err_mode" -> err_mode,
        "tst_mode" -> tst_mode,
        "verif_en" -> verif_en,
        "clr_dual_domain" -> clr_dual_domain,
        "arch_type" -> arch_type
      )
    ) {
  // Validation of all parameters
  require(width >= 2 && width <= 1024, "width must be in range [2, 1024]")
  require(ram_depth >= 2 && ram_depth <= 1024, "ram_depth must be in range [2, 1024]")
  require(mem_mode == 0, "mem_mode must be 0")
  require(f_sync_type >= 0 && f_sync_type <= 4, "f_sync_type must be in range [0, 4]")
  require(r_sync_type >= 0 && r_sync_type <= 4, "r_sync_type must be in range [0, 4]")
  require(clk_ratio == 1, "clk_ratio must be 1")
  require(ram_re_ext == 0, "ram_re_ext must be 0")
  require(err_mode == 0 || err_mode == 1, "err_mode must be 0 or 1")
  require(tst_mode == 0, "tst_mode must be 0")
  require(verif_en == 0, "verif_en must be 0")
  require(clr_dual_domain >= 0 && clr_dual_domain <= 1, "clr_dual_domain must be in range [0, 1]")
  require(arch_type == 0, "arch_type must be 0")
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
    val data_d:          UInt  = Output(UInt(width.W)) // destination pop data
    val wr_en_s_n:       Bool  = Output(Bool()) // source write enable to ram
    val wr_addr_s:       UInt  = Output(UInt(log2Ceil(ram_depth).W)) // source write address to ram
    val ram_re_d_n:      Bool  = Output(Bool()) // destination read enable to ram
    val rd_addr_d:       UInt  = Output(UInt(log2Ceil(ram_depth).W)) // destination read address to ram
    val rd_data_d:       UInt  = Input(UInt(width.W)) // destination data from fifo
    val clr_s:           Bool  = Input(Bool()) // source initiated clear ram
    val clr_sync_s:      Bool  = Output(Bool()) // source coordinated clear
    val clr_in_prog_s:   Bool  = Output(Bool()) // source clear in progress
    val clr_cmplt_s:     Bool  = Output(Bool()) // source clear complete status pulse
    val clr_d:           Bool  = Input(Bool()) // destination initiated clear ram
    val clr_sync_d:      Bool  = Output(Bool()) // destination coordinated clear
    val clr_in_prog_d:   Bool  = Output(Bool()) // destination clear in progress
    val clr_cmplt_d:     Bool  = Output(Bool()) // destination clear complete status pulse
    val ae_level_s:      UInt  = Input(UInt(log2Ceil(ram_depth + 1).W)) // source almost empty level
    val af_level_s:      UInt  = Input(UInt(log2Ceil(ram_depth + 1).W)) // source almost full level
    val fifo_empty_s:    Bool  = Output(Bool()) // source fifo (ram + cache) empty flag
    val empty_s:         Bool  = Output(Bool()) // source ram empty flag
    val almost_empty_s:  Bool  = Output(Bool()) // source ram almost empty flag
    val half_full_s:     Bool  = Output(Bool()) // source ram half full flag
    val almost_full_s:   Bool  = Output(Bool()) // source ram almost full flag
    val full_s:          Bool  = Output(Bool()) // source ram full flag
    val error_s:         Bool  = Output(Bool()) // source push error flag
    val ram_word_cnt_s:  UInt  = Output(UInt(log2Ceil(ram_depth + 1).W)) // source ram word count
    val fifo_word_cnt_s: UInt  = Output(UInt(log2Ceil(ram_depth + 1).W)) // source fifo word count
    val ae_level_d:      UInt  = Input(UInt(log2Ceil(ram_depth + 1).W)) // destination almost empty level
    val af_level_d:      UInt  = Input(UInt(log2Ceil(ram_depth + 1).W)) // destination almost full level
    val empty_d:         Bool  = Output(Bool()) // destination fifo empty flag
    val almost_empty_d:  Bool  = Output(Bool()) // destination fifo almost empty flag
    val half_full_d:     Bool  = Output(Bool()) // destination fifo half full flag
    val almost_full_d:   Bool  = Output(Bool()) // destination fifo almost full flag
    val full_d:          Bool  = Output(Bool()) // destination fifo full flag
    val error_d:         Bool  = Output(Bool()) // destination pop  error flag
    val ram_word_cnt_d:  UInt  = Output(UInt(log2Ceil(ram_depth + 1).W)) // destination ram  word count
    val fifo_word_cnt_d: UInt  = Output(UInt(log2Ceil(ram_depth + 1).W)) // destination fifo_word count
    val test:            Bool  = Input(Bool()) // scan test mode select
  })
}
