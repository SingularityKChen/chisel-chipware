import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_stackctl ==
  *
  * === Abstract ===
  *
  * Synchronous (Single Clock) Stack Controller
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | depth      | 2 to 2<sup>24</sup>    | 8        | Number of memory elements in the stack |
  * | err_mode   | 0 or 1       | 0        | Error mode |
  * | rst_mode   | 0 or 1       | 0        | Reset mode |
  *
  * === Ports ===
  *
  * | Name       | Width        | Direction | Description  |
  * |------------|--------------|-----------|--------------|
  * | clk        | 1 bit        | Input     | Input clock  |
  * | rst_n      | 1 bit        | Input     | Reset input  |
  * | push_req_n | 1 bit        | Input     | Stack push request |
  * | pop_req_n  | 1 bit        | Input     | Stack pop request |
  * | we_n       | 1 bit        | Output    | Write enable for RAM write port |
  * | empty      | 1 bit        | Output    | Stack empty flag |
  * | full       | 1 bit        | Output    | Stack full flag |
  * | error      | 1 bit        | Output    | Stack error output |
  * | wr_addr    | log2Ceil(depth) | Output | Address output to write port of RAM |
  * | rd_addr    | log2Ceil(depth) | Output | Address output to read port of RAM |
  *
  * @param depth     Number of memory elements in the stack
  * @param err_mode  Error mode
  * @param rst_mode  Reset mode
  */
class CW_stackctl(val depth: Int = 8, val err_mode: Int = 0, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "depth" -> depth,
        "err_mode" -> err_mode,
        "rst_mode" -> rst_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(depth >= 2 && depth <= 16777216, "depth must be in range [2, 16777216]")
  require(err_mode == 0 || err_mode == 1, "err_mode must be 0 or 1")
  require(rst_mode == 0 || rst_mode == 1, "rst_mode must be 0 or 1")

  val log_depth: Int = log2Ceil(depth)

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:        Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val push_req_n: Bool  = Input(Bool())
    val pop_req_n:  Bool  = Input(Bool())
    val we_n:       Bool  = Output(Bool())
    val empty:      Bool  = Output(Bool())
    val full:       Bool  = Output(Bool())
    val error:      Bool  = Output(Bool())
    val wr_addr:    UInt  = Output(UInt(log_depth.W))
    val rd_addr:    UInt  = Output(UInt(log_depth.W))
  })
}
