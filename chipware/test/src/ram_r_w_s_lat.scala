// filename: ram_r_w_s_lat.scala
import chisel3._
import chisel3.util._

class ram_r_w_s_lat(val data_width: Int = 16, val depth: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val cs_n:     Bool  = Input(Bool())
    val wr_n:     Bool  = Input(Bool())
    val rd_addr:  UInt  = Input(UInt(log2Ceil(depth).W))
    val wr_addr:  UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:  UInt  = Input(UInt(data_width.W))
    val data_out: UInt  = Output(UInt(data_width.W))
  })

  protected val U1: CW_ram_r_w_s_lat = Module(new CW_ram_r_w_s_lat(data_width, depth))
  U1.io.clk     := io.clk
  U1.io.cs_n    := io.cs_n
  U1.io.wr_n    := io.wr_n
  U1.io.rd_addr := io.rd_addr
  U1.io.wr_addr := io.wr_addr
  U1.io.data_in := io.data_in
  io.data_out   := U1.io.data_out
}
