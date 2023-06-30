// filename: ram_2r_w_s_dff.scala
import chisel3._
import chisel3.util.log2Ceil
import circt.stage._
import utest._

class ram_2r_w_s_dff(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val cs_n:         Bool  = Input(Bool())
    val wr_n:         Bool  = Input(Bool())
    val rd1_addr:     UInt  = Input(UInt(log2Ceil(depth).W))
    val rd2_addr:     UInt  = Input(UInt(log2Ceil(depth).W))
    val wr_addr:      UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:      UInt  = Input(UInt(data_width.W))
    val data_rd1_out: UInt  = Output(UInt(data_width.W))
    val data_rd2_out: UInt  = Output(UInt(data_width.W))
  })

  protected val U1: CW_ram_2r_w_s_dff = Module(new CW_ram_2r_w_s_dff(data_width, depth, rst_mode))
  U1.io.clk       := io.clk
  U1.io.rst_n     := io.rst_n
  U1.io.cs_n      := io.cs_n
  U1.io.wr_n      := io.wr_n
  U1.io.rd1_addr  := io.rd1_addr
  U1.io.rd2_addr  := io.rd2_addr
  U1.io.wr_addr   := io.wr_addr
  U1.io.data_in   := io.data_in
  io.data_rd1_out := U1.io.data_rd1_out
  io.data_rd2_out := U1.io.data_rd2_out
}

object ram_2r_w_s_dff extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ram_2r_w_s_dff") {
      def top = new ram_2r_w_s_dff()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
