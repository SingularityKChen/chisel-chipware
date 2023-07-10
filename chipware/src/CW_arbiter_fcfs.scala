import chisel3._
import chisel3.util.log2Ceil
import chisel3.experimental._ // To enable experimental features

/**
  * == CW_arbiter_fcfs ==
  *
  * === Abstract ===
  *
  * Arbiter with First-Come-First-Serve priority scheme
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Values | Description                                          |
  * |--------------|--------------|------------------------------------------------------|
  * | n            | 2 to 32      | Number of requesters                                 |
  * | park_mode    | 0 or 1       | When 1 means enable parking                          |
  * | park_index   | 0 to n-1     | Index of requester for parking                       |
  * | output_mode  | 0 or 1       | When 1, includes output registers                    |
  *
  * === Ports ===
  *
  * | Pin Name     | Width              | Direction | Function                                                     |
  * |--------------|--------------------|-----------|--------------------------------------------------------------|
  * | clk          | 1                  | Input     | Input clock                                                  |
  * | rst_n        | 1                  | Input     | Reset, active low                                            |
  * | request      | n                  | Input     | Input request                                                |
  * | lock         | n                  | Input     | Signal to lock the request to the current request, active high|
  * | mask         | n                  | Input     | Signal to mask the request, active high                      |
  * | parked       | 1                  | Output    | Flag to indicate that no one is requesting and grant given to |
  * |              |                    |           | requester indicated by park_index                            |
  * | granted      | 1                  | Output    | Flag to indicate that grant has been issued                  |
  * | locked       | 1                  | Output    | Flag to indicate that arbiter has been locked                |
  * | grant        | n                  | Output    | Output grant                                                 |
  * | grant_index  | ceil(log n)        | Output    | Index of the requester currently granted/parked              |
  *
  * @param n           Number of requesters
  * @param park_mode   When 1 means enable parking
  * @param park_index  Index of requester for parking
  * @param output_mode When 1, includes output registers
  */

class CW_arbiter_fcfs(val n: Int = 4, val park_mode: Int = 1, val park_index: Int = 0, val output_mode: Int = 1)
    extends BlackBox(
      Map(
        "n" -> n,
        "park_mode" -> park_mode,
        "park_index" -> park_index,
        "output_mode" -> output_mode
      )
    ) {
  require(n >= 2 && n <= 32, "n must be between 2 and 32")
  require(park_mode >= 0 && park_mode <= 1, "park_mode must be either 0 or 1")
  require(park_index >= 0 && park_index <= n - 1, "park_index must be between 0 and n-1")
  require(output_mode >= 0 && output_mode <= 1, "output_mode must be either 0 or 1")

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
    val grant_index: UInt  = Output(UInt(log2Ceil(n).W))
  })
}
