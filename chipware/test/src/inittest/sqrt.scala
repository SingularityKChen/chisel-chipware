import chisel3._
import circt.stage._
import utest._

class sqrt(val width: Int = 8, val tc_mode: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(width.W))
    val root: UInt = Output(UInt(((width + 1) / 2).W))
  })

  protected val U1: CW_sqrt = Module(new CW_sqrt(width, tc_mode))
  U1.io.a := io.a
  io.root := U1.io.root
}

object sqrt extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate sqrt") {
      def top = new sqrt(32, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
