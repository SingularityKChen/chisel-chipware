import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class lod(val a_width: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val a:   UInt = Input(UInt(a_width.W))
    val enc: UInt = Output(UInt((log2Ceil(a_width) + 1).W))
    val dec: UInt = Output(UInt(a_width.W))
  })

  protected val U1: CW_lod = Module(new CW_lod(a_width))
  U1.io.a := io.a
  io.enc  := U1.io.enc
  io.dec  := U1.io.dec
}

object lod extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate lod") {
      def top = new lod(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
