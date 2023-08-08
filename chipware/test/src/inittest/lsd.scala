import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class lsd(val a_width: Int = 8) extends RawModule {
  protected val log_a_width: Int = log2Ceil(a_width)

  val io = IO(new Bundle {
    val a:   UInt = Input(UInt(a_width.W))
    val enc: UInt = Output(UInt(log_a_width.W))
    val dec: UInt = Output(UInt(a_width.W))
  })

  protected val U1: CW_lsd = Module(new CW_lsd(a_width))
  U1.io.a := io.a
  io.enc  := U1.io.enc
  io.dec  := U1.io.dec
}

object lsd extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate lsd") {
      def top = new lsd(8) // Change the width value as needed

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
