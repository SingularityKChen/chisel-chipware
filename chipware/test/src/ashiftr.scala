import chisel3._
import circt.stage._
import utest._

class ashiftr(wA: Int = 2, wSH: Int = 1) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val SH: UInt = Input(UInt(wSH.W))
    val Z:  UInt = Output(UInt(wA.W))
  })

  protected val U0: CW_ashiftr = Module(new CW_ashiftr(wA, wSH))
  U0.io.A  := io.A
  U0.io.SH := io.SH
  io.Z     := U0.io.Z
}

object ashiftr extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ashiftr") {
      def top = new ashiftr()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
