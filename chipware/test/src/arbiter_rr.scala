import chisel3._
import chisel3.util._

class arbiter_rr(val num_req: Int = 4, val reg_output: Int = 1, val index_mode: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val clk: Clock = Input(Clock())
    val rst_n: Bool = Input(Bool())
    val init_n: Bool = Input(Bool())
    val enable: Bool = Input(Bool())
    val cw_arb_request: UInt = Input(UInt(num_req.W))
    val cw_arb_mask: UInt = Input(UInt(num_req.W))
    val cw_arb_granted: Bool = Output(Bool())
    val cw_arb_grant: UInt = Output(UInt(num_req.W))
    val cw_arb_grant_index: UInt = Output(UInt(log2Ceil(num_req).W))
  })

  protected val U1: CW_arbiter_rr = Module(new CW_arbiter_rr(num_req, reg_output, index_mode))

  U1.io.clk := io.clk
  U1.io.rst_n := io.rst_n
  U1.io.init_n := io.init_n
  U1.io.enable := io.enable
  U1.io.cw_arb_request := io.cw_arb_request
  U1.io.cw_arb_mask := io.cw_arb_mask
  io.cw_arb_granted := U1.io.cw_arb_granted
  io.cw_arb_grant := U1.io.cw_arb_grant
  io.cw_arb_grant_index := U1.io.cw_arb_grant_index
}
