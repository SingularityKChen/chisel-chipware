import chisel3._
import circt.stage._
import utest._

class bin2gray(val WIDTH: Int = 4) extends RawModule {
  val io = IO(new Bundle {
    val b: UInt = Input(UInt(WIDTH.W))
    val g: UInt = Output(UInt(WIDTH.W))
  })

  protected val U1: CW_bin2gray = Module(new CW_bin2gray(WIDTH))
  U1.io.b := io.b
  io.g    := U1.io.g
}

object bin2gray extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate bin2gray") {
      def top = new bin2gray()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
