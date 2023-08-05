import chisel3._
import chisel3.util._
import chisel3.experimental._

/**
  * == CW_reset_sync ==
  *
  * === Abstract ===
  *
  * Co-ordinated clearing mechanism between 2 asynchronous clock domains.
  *
  * === Parameters ===
  *
  * | Parameter      | Legal Range  | Default  | Description                            |
  * |----------------|--------------|----------|----------------------------------------|
  * | f_sync_type    | [0, 4]       | 2        | Type of synchronization mechanism     |
  * | r_sync_type    | [0, 4]       | 2        | Type of reset synchronization         |
  * | clk_d_faster   | 1            | 1        | Clock domain relationship             |
  * | reg_in_prog    | [0, 1]       | 1        | "in progress" output status signals  |
  * | tst_mode       | [0, 1]       | 1        | Scan test mode select input           |
  * | verif_en       | 0            | 0        | Random sampling error insertion      |
  *
  * === Ports ===
  *
  * | Name          | Width  | Direction  | Description                            |
  * |---------------|--------|------------|----------------------------------------|
  * | clk_s         | 1 bit  | input      | source domain clock source             |
  * | rst_s_n       | 1 bit  | input      | source domain asynchronous reset (low) |
  * | init_s_n      | 1 bit  | input      | source domain synchronous reset (low)  |
  * | clr_s         | 1 bit  | input      | source domain clear                    |
  * | clk_d         | 1 bit  | input      | destination domain clock source        |
  * | rst_d_n       | 1 bit  | input      | destination domain asynchronous reset |
  * | init_d_n      | 1 bit  | input      | destination domain synchronous reset  |
  * | clr_d         | 1 bit  | input      | destination domain clear                |
  * | test          | 1 bit  | input      | scan test mode select input             |
  * | clr_sync_s    | 1 bit  | output     | source domain clear for sequential logic |
  * | clr_in_prog_s | 1 bit  | output     | source domain clear sequence in progress |
  * | clr_cmplt_s   | 1 bit  | output     | source domain clear sequence complete  |
  * | clr_in_prog_d | 1 bit  | output     | destination domain clear sequence in progress |
  * | clr_sync_d    | 1 bit  | output     | destination domain clear for sequential logic |
  * | clr_cmplt_d   | 1 bit  | output     | destination domain clear sequence complete |
  *
  * @param f_sync_type  Type of synchronization mechanism
  * @param r_sync_type  Type of reset synchronization
  * @param clk_d_faster Clock domain relationship
  * @param reg_in_prog  "in progress" output status signals
  * @param tst_mode     Scan test mode select input
  * @param verif_en     Random sampling error insertion
  */
class CW_reset_sync(
  val f_sync_type:  Int = 2,
  val r_sync_type:  Int = 2,
  val clk_d_faster: Int = 1,
  val reg_in_prog:  Int = 1,
  val tst_mode:     Int = 1,
  val verif_en:     Int = 0)
    extends BlackBox(
      Map(
        "f_sync_type" -> f_sync_type,
        "r_sync_type" -> r_sync_type,
        "clk_d_faster" -> clk_d_faster,
        "reg_in_prog" -> reg_in_prog,
        "tst_mode" -> tst_mode,
        "verif_en" -> verif_en
      )
    )
    with HasBlackBoxPath {

  // Validation of all parameters
  require(f_sync_type >= 0 && f_sync_type <= 4, s"f_sync_type must be in range [0, 4]")
  require(r_sync_type >= 0 && r_sync_type <= 4, s"r_sync_type must be in range [0, 4]")
  require(clk_d_faster == 1, "clk_d_faster must be 1")
  require(reg_in_prog >= 0 && reg_in_prog <= 1, s"reg_in_prog must be in range [0, 1]")
  require(tst_mode >= 0 && tst_mode <= 1, s"tst_mode must be in range [0, 1]")
  require(verif_en == 0, "verif_en must be 0")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk_s:         Clock = Input(Clock()) // source domain clock source
    val rst_s_n:       Bool  = Input(Bool()) // source domain asynchronous reset (low)
    val init_s_n:      Bool  = Input(Bool()) // source domain synchronous reset (low)
    val clr_s:         Bool  = Input(Bool()) // source domain clear
    val clk_d:         Clock = Input(Clock()) // destination domain clock source
    val rst_d_n:       Bool  = Input(Bool()) // destination domain asynchronous reset
    val init_d_n:      Bool  = Input(Bool()) // destination domain synchronous reset
    val clr_d:         Bool  = Input(Bool()) // destination domain clear
    val test:          Bool  = Input(Bool()) // scan test mode select input
    val clr_sync_s:    Bool  = Output(Bool()) // source domain clear for sequential logic
    val clr_in_prog_s: Bool  = Output(Bool()) // source domain clear sequence in progress
    val clr_cmplt_s:   Bool  = Output(Bool()) // source domain clear sequence complete
    val clr_in_prog_d: Bool  = Output(Bool()) // destination domain clear sequence in progress
    val clr_sync_d:    Bool  = Output(Bool()) // destination domain clear for sequential logic
    val clr_cmplt_d:   Bool  = Output(Bool()) // destination domain clear sequence complete
  })
}
