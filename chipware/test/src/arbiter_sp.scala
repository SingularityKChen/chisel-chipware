import chisel3._
import chisel3.util.log2Ceil

// Define a parameterized Chisel Module
class arbiter_sp(val n: Int, val park_mode: Int, val park_index: Int, val output_mode: Int) extends RawModule {
  // Declare the parameters
  protected val index_width: Int = log2Ceil(n)
  val io = IO(new Bundle {
    // Declare the ports
    val clk: Clock = Input(Clock())
    val rst_n: Bool = Input(Bool())
    val request: UInt = Input(UInt(n.W))
    val lock: UInt = Input(UInt(n.W))
    val mask: UInt = Input(UInt(n.W))
    val parked: Bool = Output(Bool())
    val granted: Bool = Output(Bool())
    val locked: Bool = Output(Bool())
    val grant: UInt = Output(UInt(n.W))
    val grant_index: UInt = Output(UInt(index_width.W))
  })

  // Instantiate the Chisel BlackBox class
  protected val U1: CW_arbiter_sp = Module(new CW_arbiter_sp(n, park_mode, park_index, output_mode))

  // Connect the ports between the module and the BlackBox instance
  U1.io.clk := io.clk
  U1.io.rst_n := io.rst_n
  U1.io.request := io.request
  U1.io.lock := io.lock
  U1.io.mask := io.mask
  io.parked := U1.io.parked
  io.granted := U1.io.granted
  io.locked := U1.io.locked
  io.grant := U1.io.grant
  io.grant_index := U1.io.grant_index
}
