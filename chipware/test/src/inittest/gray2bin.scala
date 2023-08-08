import chisel3._
import circt.stage._
import utest._

class gray2bin(val WIDTH: Int = 3) extends RawModule {
  val io = IO(new Bundle {
    val g: UInt = Input(UInt(WIDTH.W))
    val b: UInt = Output(UInt(WIDTH.W))
  })

  protected val U1: CW_gray2bin = Module(new CW_gray2bin(WIDTH))
  U1.io.g := io.g
  io.b    := U1.io.b
}

object gray2bin extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate gray2bin") {
      def top = new gray2bin(3)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
