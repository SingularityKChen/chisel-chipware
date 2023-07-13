import chisel3._
import circt.stage._
import utest._

class ff(val wD: Int = 1) extends RawModule {
  val io = IO(new Bundle {
    val CLK: Clock = Input(Clock())
    val D:   UInt  = Input(UInt(wD.W))
    val Q:   UInt  = Output(UInt(wD.W))
  })

  protected val U1: CW_ff = Module(new CW_ff(wD))
  U1.io.CLK := io.CLK
  U1.io.D   := io.D
  io.Q      := U1.io.Q
}

object ff extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ff") {
      def top = new ff(1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
