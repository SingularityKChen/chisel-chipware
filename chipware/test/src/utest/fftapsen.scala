import chisel3._
import circt.stage._
import utest._

class fftapsen(val wD: Int = 1, val stages: Int = 1) extends RawModule {
  protected val bit_width_wD: Int = wD
  val io = IO(new Bundle {
    val CLK: Clock = Input(Clock())
    val EN:  Bool  = Input(Bool())
    val D:   UInt  = Input(UInt(wD.W))
    val Q:   UInt  = Output(UInt((stages * wD).W))
  })

  protected val U1: CW_fftapsen = Module(new CW_fftapsen(wD, stages))
  U1.io.CLK := io.CLK
  U1.io.EN  := io.EN
  U1.io.D   := io.D
  io.Q      := U1.io.Q
}

object fftapsen extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fftapsen") {
      def top = new fftapsen(1, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
