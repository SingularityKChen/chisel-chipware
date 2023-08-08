import chisel3._
import circt.stage._
import utest._

class thermdec(val width: Int = 3) extends RawModule {
  val io = IO(new Bundle {
    val en: Bool = Input(Bool())
    val a:  UInt = Input(UInt(width.W))
    val b:  UInt = Output(UInt((1 << width).W))
  })

  protected val U1: CW_thermdec = Module(new CW_thermdec(width))
  U1.io.en := io.en
  U1.io.a  := io.a
  io.b     := U1.io.b
}

object thermdec extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate thermdec") {
      def top = new thermdec(3)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
