import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_stack ==
  *
  * === Abstract ===
  *
  * Synchronous (Single-Clock) Stack
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | width      | 1 to 256     | 8        | Width of data_in and data_out buses |
  * | depth      | 2 to 256     | 4        | Depth (in words) of memory array |
  * | err_mode   | 0 or 1       | 0        | Error mode |
  * | rst_mode   | 0 to 3       | 0        | Reset mode |
  *
  * === Ports ===
  *
  * | Name       | Width        | Direction | Description  |
  * |------------|--------------|-----------|--------------|
  * | clk        | 1 bit        | Input     | Input clock  |
  * | rst_n      | 1 bit        | Input     | Reset input  |
  * | push_req_n | 1 bit        | Input     | Stack push request |
  * | pop_req_n  | 1 bit        | Input     | Stack pop request |
  * | data_in    | width bit(s) | Input     | Stack push data |
  * | empty      | 1 bit        | Output    | Stack empty flag |
  * | full       | 1 bit        | Output    | Stack full flag |
  * | error      | 1 bit        | Output    | Stack error output |
  * | data_out   | width bit(s) | Output    | Stack pop data |
  *
  * @param width     Width of data_in and data_out buses
  * @param depth     Depth (in words) of memory array
  * @param err_mode  Error mode
  * @param rst_mode  Reset mode
  */
class CW_stack(val width: Int = 8, val depth: Int = 4, val err_mode: Int = 0, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "depth" -> depth,
        "err_mode" -> err_mode,
        "rst_mode" -> rst_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 1 && width <= 256, "width must be in range [1, 256]")
  require(depth >= 2 && depth <= 256, "depth must be in range [2, 256]")
  require(err_mode == 0 || err_mode == 1, "err_mode must be 0 or 1")
  require(rst_mode >= 0 && rst_mode <= 3, "rst_mode must be in range [0, 3]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:        Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val push_req_n: Bool  = Input(Bool())
    val pop_req_n:  Bool  = Input(Bool())
    val data_in:    UInt  = Input(UInt(width.W))
    val empty:      Bool  = Output(Bool())
    val full:       Bool  = Output(Bool())
    val error:      Bool  = Output(Bool())
    val data_out:   UInt  = Output(UInt(width.W))
  })
}
