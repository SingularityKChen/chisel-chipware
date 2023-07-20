import chisel3._
import circt.stage._
import utest._

class fp_sub(val sig_width: Int = 23, val exp_width: Int = 8, val ieee_compliance: Int = 1) extends RawModule {
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd:    UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt((sig_width + exp_width + 1).W))
  })

  protected val U1: CW_fp_sub = Module(new CW_fp_sub(sig_width, exp_width, ieee_compliance))
  U1.io.a   := io.a
  U1.io.b   := io.b
  U1.io.rnd := io.rnd
  io.status := U1.io.status
  io.z      := U1.io.z
}

object fp_sub extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fp_sub") {
      def top = new fp_sub()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
