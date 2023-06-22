import chisel3._
import chisel3.util._
import chisel3.experimental._ // To enable experimental features

/**
  * CW_arbiter_sp
  *
  * === Parameters ===
  * | Parameter | Values           | Description                           |
  * | ---       | ---              | ---                                   |
  * | n         | 2 to 32 (def:4)  | Number of requesters                  |
  * | park_mode | 0 or 1  (def:1)  | Enable parking mode                   |
  * | park_index| 0 to n-1(def:0)  | Index of requester for parking        |
  * | output_mode| 0 or 1  (def:1)  | Include output registers              |
  *
  * === Ports ===
  * | Port       | Width      | Direction | Function                              |
  * | ---        | ---        | ---       | ---                                   |
  * | clk        | 1          | Input     | Input clock                           |
  * | rst_n      | 1          | Input     | Reset, active low                     |
  * | request    | n          | Input     | Input request                         |
  * | lock       | n          | Input     | Signal to lock the request            |
  * | mask       | n          | Input     | Signal to mask the request            |
  * | parked     | 1          | Output    | Flag to indicate no one is requesting |
  * | granted    | 1          | Output    | Flag to indicate grant has been issued |
  * | locked     | 1          | Output    | Flag to indicate arbiter has been locked |
  * | grant      | n          | Output    | Output grant                          |
  * | grant_index| ceil(log n) | Output    | Index of the requester currently granted/parked |
  *
  * @param n Number of requesters
  * @param park_mode Enable parking mode
  * @param park_index Index of requester for parking
  * @param output_mode Include output registers
  */
class CW_arbiter_sp(n: Int, park_mode: Int, park_index: Int, output_mode: Int)
    extends BlackBox(
      Map(
        "n" -> 4,
        "park_mode" -> 1,
        "park_index" -> 0,
        "output_mode" -> 1
      )
    ) {
  protected val index_width: Int = log2Ceil(n)

  val io = IO(new Bundle {
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val request:     UInt  = Input(UInt(n.W))
    val lock:        UInt  = Input(UInt(n.W))
    val mask:        UInt  = Input(UInt(n.W))
    val parked:      Bool  = Output(Bool())
    val granted:     Bool  = Output(Bool())
    val locked:      Bool  = Output(Bool())
    val grant:       UInt  = Output(UInt(n.W))
    val grant_index: UInt  = Output(UInt(index_width.W))
  })
}
