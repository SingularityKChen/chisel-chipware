import chisel3._
import chisel3.util.log2Ceil

class arbiter_fcfs(val n: Int = 4, val park_mode: Int = 1, val park_index: Int = 0, val output_mode: Int = 1)
    extends RawModule {
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

  protected val U1: CW_arbiter_fcfs = Module(new CW_arbiter_fcfs(n, park_mode, park_index, output_mode))

  U1.io.clk     := io.clk
  U1.io.rst_n   := io.rst_n
  U1.io.request := io.request
  U1.io.lock    := io.lock
  U1.io.mask    := io.mask

  io.parked      := U1.io.parked
  io.granted     := U1.io.granted
  io.locked      := U1.io.locked
  io.grant       := U1.io.grant
  io.grant_index := U1.io.grant_index
}
