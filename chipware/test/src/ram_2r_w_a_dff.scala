import chisel3._
import chisel3.util.log2Ceil
import circt.stage._
import utest._

class ram_2r_w_a_dff(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0) extends RawModule {

  protected val log_depth: Int = log2Ceil(depth)

  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_n:        Bool  = Input(Bool())
    val cs_n:         Bool  = Input(Bool())
    val wr_n:         Bool  = Input(Bool())
    val test_mode:    Bool  = Input(Bool())
    val test_clk:     Clock = Input(Clock())
    val rd1_addr:     UInt  = Input(UInt(log_depth.W))
    val rd2_addr:     UInt  = Input(UInt(log_depth.W))
    val wr_addr:      UInt  = Input(UInt(log_depth.W))
    val data_in:      UInt  = Input(UInt(data_width.W))
    val data_rd1_out: UInt  = Output(UInt(data_width.W))
    val data_rd2_out: UInt  = Output(UInt(data_width.W))
  })

  val U0: CW_ram_2r_w_a_dff = Module(new CW_ram_2r_w_a_dff(data_width, depth, rst_mode))

  U0.io.rst_n     := io.rst_n
  U0.io.cs_n      := io.cs_n
  U0.io.wr_n      := io.wr_n
  U0.io.test_mode := io.test_mode
  U0.io.test_clk  := io.test_clk
  U0.io.rd1_addr  := io.rd1_addr
  U0.io.rd2_addr  := io.rd2_addr
  U0.io.wr_addr   := io.wr_addr
  U0.io.data_in   := io.data_in

  io.data_rd1_out := U0.io.data_rd1_out
  io.data_rd2_out := U0.io.data_rd2_out
}

object ram_2r_w_a_dff extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ram_2r_w_a_dff") {
      def top = new ram_2r_w_a_dff()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
