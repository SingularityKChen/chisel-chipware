import chisel3._
import chisel3.util.{log2Ceil, HasBlackBoxPath}
import chisel3.experimental._ // To enable experimental features

/**
  * == CW_arbiter_dp ==
  *
  * === Abstract ===
  *
  * Arbiter with Dynamic priority scheme
  *
  * === Parameters ===
  * | Parameter   | Legal Values      | Description                  |
  * |-------------|-------------------|------------------------------|
  * | n           | 2 to 32 (def:4)   | Number of requesters         |
  * | park_mode   | 0 or 1  (def:1)   | Enable parking mode          |
  * | park_index  | 0 to n-1(def:0)   | Index of requester for parking|
  * | output_mode | 0 or 1  (def:1)   | Include output registers     |
  *
  * === Ports ===
  * | Port         | Bit Width              | Direction | Function                                  |
  * |--------------|------------------------|-----------|-------------------------------------------|
  * | clk          | 1                      | Input     | Input clock                               |
  * | rst_n        | 1                      | Input     | Reset, active low                         |
  * | init_n       | 1                      | Input     | Synchronous signal to clear all registers |
  * | enable       | 1                      | Input     | Active-high signal to enable all registers|
  * | request      | n                      | Input     | Input request                             |
  * | prior        | n * ceil(log n)        | Input     | Priority input vector                     |
  * | lock         | n                      | Input     | Signal to lock the request                |
  * | mask         | n                      | Input     | Signal to mask the request                |
  * | parked       | 1                      | Output    | Flag to indicate no request and grant given|
  * | granted      | 1                      | Output    | Flag to indicate grant issued             |
  * | locked       | 1                      | Output    | Flag to indicate arbiter locked           |
  * | grant        | n                      | Output    | Output grant                              |
  * | grant_index  | ceil(log n)            | Output    | Index of the requester currently granted  |
  *
  * @param n           Number of requesters (default: 4)
  * @param park_mode   Enable parking mode (default: 1)
  * @param park_index  Index of requester for parking (default: 0)
  * @param output_mode Include output registers (default: 1)
  */
class CW_arbiter_dp(val n: Int = 4, val park_mode: Int = 1, val park_index: Int = 0, val output_mode: Int = 1)
    extends BlackBox(
      Map(
        "n" -> n,
        "park_mode" -> park_mode,
        "park_index" -> park_index,
        "output_mode" -> output_mode
      )
    )
    with HasBlackBoxPath {
  require(n >= 2 && n <= 32, "n must be in the range [2, 32]")
  require(park_mode == 0 || park_mode == 1, "park_mode must be either 0 or 1")
  require(park_index >= 0 && park_index < n, "park_index must be in the range [0, n-1]")
  require(output_mode == 0 || output_mode == 1, "output_mode must be either 0 or 1")
  protected val index_width: Int = log2Ceil(n)
  val io = IO(new Bundle {
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val init_n:      Bool  = Input(Bool())
    val enable:      Bool  = Input(Bool())
    val request:     UInt  = Input(UInt(n.W))
    val lock:        UInt  = Input(UInt(n.W))
    val mask:        UInt  = Input(UInt(n.W))
    val prior:       UInt  = Input(UInt((n * index_width).W))
    val parked:      Bool  = Output(Bool())
    val granted:     Bool  = Output(Bool())
    val locked:      Bool  = Output(Bool())
    val grant:       UInt  = Output(UInt(n.W))
    val grant_index: UInt  = Output(UInt(index_width.W))
  })
}
