import chisel3._
import circt.stage._
import utest._

class pl_reg(val width: Int = 1, val in_reg: Int = 0, val stages: Int = 1, val out_reg: Int = 0, val rst_mode: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val rst_n:    Bool  = Input(Bool())
    val enable:   UInt  = Input(UInt((stages - 1 + in_reg + out_reg).W))
    val data_in:  UInt  = Input(UInt(width.W))
    val data_out: UInt  = Output(UInt(width.W))
  })

  protected val U1: CW_pl_reg = Module(new CW_pl_reg(width, in_reg, stages, out_reg, rst_mode))
  U1.io.clk     := io.clk
  U1.io.rst_n   := io.rst_n
  U1.io.enable  := io.enable
  U1.io.data_in := io.data_in
  io.data_out   := U1.io.data_out
}

object pl_reg extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate pl_reg") {
      def top = new pl_reg(1, 0, 1, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
