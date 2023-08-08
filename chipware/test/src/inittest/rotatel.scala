import chisel3._
import circt.stage._
import utest._

class rotatel(val wA: Int, val wSH: Int) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val SH: UInt = Input(UInt(wSH.W))
    val Z:  UInt = Output(UInt(wA.W))
  })

  // Instantiate the BlackBox
  protected val U1: CW_rotatel = Module(new CW_rotatel(wA, wSH))
  U1.io.A  := io.A
  U1.io.SH := io.SH
  io.Z     := U1.io.Z
}

object rotatel extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate rotatel") {
      def top = new rotatel(2, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
