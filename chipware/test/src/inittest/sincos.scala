import chisel3._
import circt.stage._
import utest._

class sincos(val A_width: Int = 34, val WAVE_width: Int = 34) extends RawModule {
  val io = IO(new Bundle {
    val A:       UInt = Input(UInt(A_width.W))
    val SIN_COS: Bool = Input(Bool())
    val WAVE:    UInt = Output(UInt(WAVE_width.W))
  })

  protected val U1: CW_sincos = Module(new CW_sincos(A_width, WAVE_width))
  U1.io.A       := io.A
  U1.io.SIN_COS := io.SIN_COS
  io.WAVE       := U1.io.WAVE
}

object sincos extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate sincos") {
      def top = new sincos(34, 34)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
