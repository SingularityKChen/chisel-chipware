import chisel3._
import chisel3.util.log2Ceil

class ram_2r_w_a_lat(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0) extends RawModule {
  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_n:        Bool = Input(Bool())
    val cs_n:         Bool = Input(Bool())
    val wr_n:         Bool = Input(Bool())
    val rd1_addr:     UInt = Input(UInt(log2Ceil(depth).W))
    val rd2_addr:     UInt = Input(UInt(log2Ceil(depth).W))
    val wr_addr:      UInt = Input(UInt(log2Ceil(depth).W))
    val data_in:      UInt = Input(UInt(data_width.W))
    val data_rd1_out: UInt = Output(UInt(data_width.W))
    val data_rd2_out: UInt = Output(UInt(data_width.W))
  })

  // Create a BlackBox instance
  val U0: CW_ram_2r_w_a_lat = Module(new CW_ram_2r_w_a_lat(data_width, depth, rst_mode))

  // Connect the ports
  U0.io.rst_n     := io.rst_n
  U0.io.cs_n      := io.cs_n
  U0.io.wr_n      := io.wr_n
  U0.io.rd1_addr  := io.rd1_addr
  U0.io.rd2_addr  := io.rd2_addr
  U0.io.wr_addr   := io.wr_addr
  U0.io.data_in   := io.data_in
  io.data_rd1_out := U0.io.data_rd1_out
  io.data_rd2_out := U0.io.data_rd2_out
}
