import chisel3._
import circt.stage._
import utest._

class pipe_reg(val stages: Int = 1, val wD: Int = 1) extends RawModule {
  val io = IO(new Bundle {
    val D:   UInt  = Input(UInt(wD.W))
    val CLK: Clock = Input(Clock())
    val Q:   UInt  = Output(UInt(wD.W))
  })

  protected val U1: CW_pipe_reg = Module(new CW_pipe_reg(stages, wD))
  U1.io.D   := io.D
  U1.io.CLK := io.CLK
  io.Q      := U1.io.Q
}

object pipe_reg extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate pipe_reg") {
      def top = new pipe_reg(1, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
