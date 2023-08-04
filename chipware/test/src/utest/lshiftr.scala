import chisel3._
import circt.stage._
import utest._

class lshiftr(val wA: Int = 2, val wSH: Int = 1) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val SH: UInt = Input(UInt(wSH.W))
    val Z:  UInt = Output(UInt(wA.W))
  })

  protected val U1: CW_lshiftr = Module(new CW_lshiftr(wA, wSH))
  U1.io.A  := io.A
  U1.io.SH := io.SH
  io.Z     := U1.io.Z
}

object lshiftr extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate lshiftr") {
      def top = new lshiftr(2, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
