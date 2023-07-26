import chisel3._
import circt.stage._
import utest._

class inc_gray(val width: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val a:  UInt = Input(UInt(width.W))
    val ci: Bool = Input(Bool())
    val z:  UInt = Output(UInt(width.W))
  })

  protected val U1: CW_inc_gray = Module(new CW_inc_gray(width))
  U1.io.a  := io.a
  U1.io.ci := io.ci
  io.z     := U1.io.z
}

object inc_gray extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate inc_gray") {
      def top = new inc_gray(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
