import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class decode_en(val width: Int = 3) extends RawModule {
  val io = IO(new Bundle {
    val en: Bool = Input(Bool())
    val a:  UInt = Input(UInt(width.W))
    val b:  UInt = Output(UInt((1 << width).W))
  })

  protected val U1: CW_decode_en = Module(new CW_decode_en(width))
  U1.io.en := io.en
  U1.io.a  := io.a
  io.b     := U1.io.b
}

object decode_en extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate decode_en") {
      def top = new decode_en(3)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
