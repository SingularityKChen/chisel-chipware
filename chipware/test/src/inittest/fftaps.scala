import chisel3._
import circt.stage._
import utest._

class fftaps(val wD: Int = 1, val stages: Int = 1) extends RawModule {
  require(wD >= 1, "wD must be >= 1")
  require(stages >= 1, "stages must be >= 1")
  protected val bit_width_Q: Int = stages * wD
  val io = IO(new Bundle {
    val CLK: Clock = Input(Clock())
    val D:   UInt  = Input(UInt(wD.W))
    val Q:   UInt  = Output(UInt(bit_width_Q.W))
  })

  protected val U1: CW_fftaps = Module(new CW_fftaps(wD, stages))
  U1.io.CLK := io.CLK
  U1.io.D   := io.D
  io.Q      := U1.io.Q
}

object fftaps extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fftaps") {
      def top = new fftaps(1, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
