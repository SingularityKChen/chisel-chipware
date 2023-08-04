import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class lza(val width: Int = 4) extends RawModule {
  protected val BWBOinputWidth: Int = log2Ceil(width)

  val io = IO(new Bundle {
    val a:     UInt = Input(UInt(width.W))
    val b:     UInt = Input(UInt(width.W))
    val count: UInt = Output(UInt(BWBOinputWidth.W))
  })

  protected val U1: CW_lza = Module(new CW_lza(width))
  U1.io.a  := io.a
  U1.io.b  := io.b
  io.count := U1.io.count
}

object lza extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate lza") {
      def top = new lza(4)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
