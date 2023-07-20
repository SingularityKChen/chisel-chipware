import chisel3._
import chisel3.util._
import chisel3.experimental._ // To enable experimental features

/**
  * == CW_arbiter_2t ==
  *
  * === Abstract ===
  *
  * Two Tier Arbiter with dynamic/fair among equal scheme
  *
  * === Parameters ===
  * | Name       | Values           | Description                                                          |
  * |------------|------------------|----------------------------------------------------------------------|
  * | n          | 2 to 32          | Number of requesters                                                 |
  * | p_width    | 1 to 5           | Width of priority vector of each requester                           |
  * | park_mode  | 0 or 1           | When 1 means enable parking                                           |
  * | park_index | 0 to n-1         | Index of requester for parking                                       |
  * | output_mode| 0 or 1           | When 1, includes output registers                                    |
  *
  * === Ports ===
  * | Name        | Width     | Direction | Function                                                         |
  * |-------------|-----------|-----------|-----------------------------------------------------------------|
  * | clk         | 1         | Input     | Input clock                                                      |
  * | rst_n       | 1         | Input     | Reset, active low                                                |
  * | request     | n         | Input     | Input request                                                    |
  * | priority    | n * p_width| Input     | Priority input                                                   |
  * | lock        | n         | Input     | Signal to lock the request to the current request, active high   |
  * | mask        | n         | Input     | Signal to mask the request, active high                           |
  * | parked      | 1         | Output    | Flag to indicate that no one is requesting and grant given to     |
  * |             |           |           | requester indicated by park_index                                 |
  * | granted     | 1         | Output    | Flag to indicate that grant has been issued                        |
  * | locked      | 1         | Output    | Flag to indicate that arbiter has been locked                      |
  * | grant       | n         | Output    | Output grant                                                     |
  * | grant_index | ceil(log n)| Output    | Index of the requester currently granted/parked                   |
  *
  * @param n          Number of requesters
  * @param p_width    Width of priority vector of each requester
  * @param park_mode  When 1 means enable parking
  * @param park_index Index of requester for parking
  * @param output_mode When 1, includes output registers
  */
class CW_arbiter_2t(
  val n:           Int = 4,
  val p_width:     Int = 2,
  val park_mode:   Int = 1,
  val park_index:  Int = 0,
  val output_mode: Int = 1)
    extends BlackBox(
      Map(
        "n" -> n,
        "p_width" -> p_width,
        "park_mode" -> park_mode,
        "park_index" -> park_index,
        "output_mode" -> output_mode
      )
    )
    with HasBlackBoxPath {
  require(n >= 2 && n <= 32, "n must be between 2 and 32 (inclusive)")
  require(p_width >= 1 && p_width <= 5, "p_width must be between 1 and 5 (inclusive)")
  require(park_mode == 0 || park_mode == 1, "park_mode must be either 0 or 1")
  require(park_index >= 0 && park_index <= n - 1, s"park_index must be between 0 and n-1 (inclusive), where n = $n")
  require(output_mode == 0 || output_mode == 1, "output_mode must be either 0 or 1")

  protected val index_width: Int = log2Ceil(n)

  val io = IO(new Bundle {
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val request:     UInt  = Input(UInt(n.W))
    val priority:    UInt  = Input(UInt((n * p_width).W))
    val lock:        UInt  = Input(UInt(n.W))
    val mask:        UInt  = Input(UInt(n.W))
    val parked:      Bool  = Output(Bool())
    val granted:     Bool  = Output(Bool())
    val locked:      Bool  = Output(Bool())
    val grant:       UInt  = Output(UInt(n.W))
    val grant_index: UInt  = Output(UInt((log2Ceil(n)).W))
  })
}
