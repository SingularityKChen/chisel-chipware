import chisel3._
import chisel3.util.log2Ceil

class ram_2r_w_s_lat(val data_width: Int = 16, val depth: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val cs_n:         Bool  = Input(Bool())
    val wr_n:         Bool  = Input(Bool())
    val rd1_addr:     UInt  = Input(UInt(log2Ceil(depth).W))
    val rd2_addr:     UInt  = Input(UInt(log2Ceil(depth).W))
    val wr_addr:      UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:      UInt  = Input(UInt(data_width.W))
    val data_rd1_out: UInt  = Output(UInt(data_width.W))
    val data_rd2_out: UInt  = Output(UInt(data_width.W))
  })

  require(data_width >= 1 && data_width <= 256, "data_width must be in the range [1, 256]")
  require(depth >= 2 && depth <= 256, "depth must be in the range [2, 256]")

  val U1: CW_ram_2r_w_s_lat = Module(new CW_ram_2r_w_s_lat(data_width, depth))
  U1.io.clk       := io.clk
  U1.io.cs_n      := io.cs_n
  U1.io.wr_n      := io.wr_n
  U1.io.rd1_addr  := io.rd1_addr
  U1.io.rd2_addr  := io.rd2_addr
  U1.io.wr_addr   := io.wr_addr
  U1.io.data_in   := io.data_in
  io.data_rd1_out := U1.io.data_rd1_out
  io.data_rd2_out := U1.io.data_rd2_out
}
