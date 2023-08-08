import chisel3._
import circt.stage._
import utest._

class ffen(val wD: Int = 1) extends RawModule {
  protected val U1: CW_ffen = Module(new CW_ffen(wD))
  val io = IO(new Bundle {
    val CLK: Clock = Input(Clock())
    val EN:  Bool  = Input(Bool())
    val D:   UInt  = Input(UInt(wD.W))
    val Q:   UInt  = Output(UInt(wD.W))
  })

  U1.io.CLK := io.CLK
  U1.io.EN  := io.EN
  U1.io.D   := io.D
  io.Q      := U1.io.Q
}

object ffen extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ffen") {
      def top = new ffen(2)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
