import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_dpll_sd ==
  *
  * === Abstract ===
  *
  * Digital Phase Lock Loop
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | width      | >= 1         | 1        | Number of input serial channels |
  * | divisor    | >= 1         | 4        | Determines the number of samples per input clock cycle |
  * | gain       | 1 to 2       | 1        | Phase correction factor |
  * | filter     | 0 to 8       | 2        | Phase correction control |
  * | windows    | 1 to (divisor+1)/2 | 2  | Number of sampling windows for input serial data stream |
  *
  * === Ports ===
  *
  * | Name       | Width        | Direction | Description  |
  * |------------|--------------|-----------|--------------|
  * | clk        | 1            | Input     | Input clock  |
  * | rst_n      | 1            | Input     | Asynchronous reset, active low |
  * | stall      | 1            | Input     | Stalls everything except synchronizer |
  * | squelch    | 1            | Input     | Turns off phase detection |
  * | window     | ceil(log2(windows)) | Input | Sampling window selector |
  * | data_in    | width        | Input     | Serial input data stream |
  * | clk_out    | 1            | Output    | Recovered clock |
  * | bit_ready  | 1            | Output    | Output data ready flag |
  * | data_out   | width        | Output    | Recovered output data stream |
  *
  * @param width Number of input serial channels
  * @param divisor Determines the number of samples per input clock cycle
  * @param gain Phase correction factor
  * @param filter Phase correction control
  * @param windows Number of sampling windows for input serial data stream
  */
class CW_dpll_sd(val width: Int = 1, val divisor: Int = 4, val gain: Int = 1, val filter: Int = 2, val windows: Int = 2)
    extends BlackBox(
      Map(
        "width" -> width,
        "divisor" -> divisor,
        "gain" -> gain,
        "filter" -> filter,
        "windows" -> windows
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 1 && width <= 16, "width must be in range [1, 16]")
  require(divisor >= 4 && divisor <= 256, "divisor must be in range [4, 256]")
  require(gain >= 1 && gain <= 2, "gain must be in range [1, 2]")
  require(filter >= 0 && filter <= 8, "filter must be in range [0, 8]")
  require(windows >= 1 && windows <= (divisor + 1) / 2, "windows must be in range [1, (divisor + 1) / 2]")

  val win_width: Int = log2Ceil(windows)

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:       Clock = Input(Clock())
    val rst_n:     Bool  = Input(Bool())
    val stall:     Bool  = Input(Bool())
    val squelch:   Bool  = Input(Bool())
    val window:    UInt  = Input(UInt(win_width.W))
    val data_in:   UInt  = Input(UInt(width.W))
    val clk_out:   Clock = Output(Clock())
    val bit_ready: Bool  = Output(Bool())
    val data_out:  UInt  = Output(UInt(width.W))
  })
}
