// filename: ram_rw_s_dff.scala
import chisel3._
import chisel3.util._

class ram_rw_s_dff(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0) extends RawModule {
  // Port declarations
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val rst_n:    Bool  = Input(Bool())
    val cs_n:     Bool  = Input(Bool())
    val wr_n:     Bool  = Input(Bool())
    val rw_addr:  UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:  UInt  = Input(UInt(data_width.W))
    val data_out: UInt  = Output(UInt(data_width.W))
  })
  // Create an instance of the BlackBox class
  protected val U1: CW_ram_rw_s_dff = Module(new CW_ram_rw_s_dff(data_width, depth, rst_mode))

  // Port connections
  U1.io.clk     := io.clk
  U1.io.rst_n   := io.rst_n
  U1.io.cs_n    := io.cs_n
  U1.io.wr_n    := io.wr_n
  U1.io.rw_addr := io.rw_addr
  U1.io.data_in := io.data_in
  io.data_out   := U1.io.data_out
}
