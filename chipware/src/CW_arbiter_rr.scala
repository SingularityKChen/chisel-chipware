import chisel3._
import chisel3.util._
import chisel3.experimental._ // To enable experimental features

/**
  * == CW_arbiter_rr ==
  *
  * === Abstract ===
  *
  *  Arbiter with Round Robin priority scheme
  *
  * === Parameters ===
  *
  * | Parameter    | Values        | Description                                                                                          |
  * |--------------|---------------|------------------------------------------------------------------------------------------------------|
  * | num_req      | 2 to 32       | Number of requesters                                                                                 |
  * | reg_output   | 0 or 1        | When 1, includes output registers                                                                    |
  * | index_mode   | 0 to 3        | - 0 or 1: Causes grant_index value to be grant bit position plus 1. The difference between index_mode |
  * |              |               | 0 and index_mode 1 is in the sizing of the grant_index output port. For index_mode = 0, the          |
  * |              |               | grant_index port is ceil(log2(n)) bits wide, but for index_mode = 1 grant_index is                   |
  * |              |               | ceil(log2(n+1)) bits wide.                                                                           |
  * |              |               | - 2: Causes grant_index values to be bit position of granted client when a grant is active and all   |
  * |              |               | 0(all zeros) when no grant is active.                                                                |
  * |              |               | - 3: Same as index_mode 2, but when no grant is active, output index is all 1(all ones).              |
  *
  * === Ports ===
  *
  * | Name               | Width         | Direction | Function                                          |
  * |--------------------|---------------|-----------|---------------------------------------------------|
  * | clk                | 1             | Input     | Input clock                                       |
  * | rst_n              | 1             | Input     | Reset, active low                                 |
  * | init_n             | 1             | Input     | Synchronous signal to clear all registers         |
  * | enable             | 1             | Input     | Active-high signal to enable all requests         |
  * | cw_arb_request     | num_req       | Input     | Input request                                     |
  * | cw_arb_mask        | num_req       | Input     | Signal to mask the request, active high           |
  * | cw_arb_granted     | 1             | Output    | Flag to indicate that grant has been issued       |
  * | cw_arb_grant       | num_req       | Output    | Output grant                                      |
  * | cw_arb_grant_index | ceil(log num_req) or ceil(log num_req+1) | Output    | Index of the requester currently granted/parked |
  *
  * @param num_req     Number of requesters
  * @param reg_output  Includes output registers when 1
  * @param index_mode  Determines the mode of the grant_index output port
  */
class CW_arbiter_rr(val num_req: Int = 4, val reg_output: Int = 1, val index_mode: Int = 0)
    extends BlackBox(
      Map(
        "num_req" -> num_req,
        "reg_output" -> reg_output,
        "index_mode" -> index_mode
      )
    )
    with HasBlackBoxPath {
  require(num_req >= 2 && num_req <= 32, "num_req must be between 2 and 32")
  require(reg_output >= 0 && reg_output <= 1, "reg_output must be 0 or 1")
  require(index_mode >= 0 && index_mode <= 3, "index_mode must be between 0 and 3")

  protected val index_width: Int = if (index_mode == 1) log2Ceil(num_req + 1) else log2Ceil(num_req)

  val io = IO(new Bundle {
    val clk:                Clock = Input(Clock())
    val rst_n:              Bool  = Input(Bool())
    val init_n:             Bool  = Input(Bool())
    val enable:             Bool  = Input(Bool())
    val cw_arb_request:     UInt  = Input(UInt(num_req.W))
    val cw_arb_mask:        UInt  = Input(UInt(num_req.W))
    val cw_arb_granted:     Bool  = Output(Bool())
    val cw_arb_grant:       UInt  = Output(UInt(num_req.W))
    val cw_arb_grant_index: UInt  = Output(UInt(index_width.W))
  })
}
