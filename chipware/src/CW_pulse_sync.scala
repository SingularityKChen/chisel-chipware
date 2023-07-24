import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_pulse_sync ==
  *
  * === Abstract ===
  *
  * Dual Clock Pulse Synchronizer
  *
  * === Parameters ===
  *
  * | Parameter   | Legal Range  | Default  | Description  |
  * |-------------|--------------|----------|--------------|
  * | reg_event   | 0 to 1       | 1        | event_d is non registered (0) or registered (1) |
  * | f_sync_type | 0 to 4       | 2        | Synchronizer type |
  * | tst_mode    | 0 to 1       | 0        | DFT scan test mode select |
  * | verif_en    | 0            | 0        | Verification enable |
  * | pulse_mode  | 0 to 3       | 0        | Pulse mode |
  *
  * === Ports ===
  *
  * | Name     | Width  | Direction | Description  |
  * |----------|--------|-----------|--------------|
  * | event_s  | 1 bit  | Input     | Source input pulse |
  * | clk_s    | 1 bit  | Input     | Source clock |
  * | init_s_n | 1 bit  | Input     | Source synchronous reset |
  * | rst_s_n  | 1 bit  | Input     | Source asynchronous reset |
  * | clk_d    | 1 bit  | Input     | Destination clock |
  * | init_d_n | 1 bit  | Input     | Destination synchronous reset |
  * | rst_d_n  | 1 bit  | Input     | Destination asynchronous reset |
  * | test     | 1 bit  | Input     | DFT scan test mode select |
  * | event_d  | 1 bit  | Output    | Destination output pulse |
  *
  * @param reg_event   Event_d register parameter
  * @param f_sync_type Synchronizer type parameter
  * @param tst_mode    Test mode parameter
  * @param verif_en    Verification enable parameter
  * @param pulse_mode  Pulse mode parameter
  */
class CW_pulse_sync(
  val reg_event:   Int = 1,
  val f_sync_type: Int = 2,
  val tst_mode:    Int = 0,
  val verif_en:    Int = 0,
  val pulse_mode:  Int = 0)
    extends BlackBox(
      Map(
        "reg_event" -> reg_event,
        "f_sync_type" -> f_sync_type,
        "tst_mode" -> tst_mode,
        "verif_en" -> verif_en,
        "pulse_mode" -> pulse_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(reg_event >= 0 && reg_event <= 1, "reg_event must be in range [0, 1]")
  require(f_sync_type >= 0 && f_sync_type <= 4, "f_sync_type must be in range [0, 4]")
  require(tst_mode >= 0 && tst_mode <= 1, "tst_mode must be in range [0, 1]")
  require(verif_en == 0, "verif_en must be 0")
  require(pulse_mode >= 0 && pulse_mode <= 3, "pulse_mode must be in range [0, 3]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val event_s:  Bool  = Input(Bool())
    val clk_s:    Clock = Input(Clock())
    val init_s_n: Bool  = Input(Bool())
    val rst_s_n:  Bool  = Input(Bool())
    val clk_d:    Clock = Input(Clock())
    val init_d_n: Bool  = Input(Bool())
    val rst_d_n:  Bool  = Input(Bool())
    val test:     Bool  = Input(Bool())
    val event_d:  Bool  = Output(Bool())
  })
}
