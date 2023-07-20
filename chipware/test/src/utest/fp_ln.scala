import chisel3._
import circt.stage._
import utest._

class fp_ln(
  val sig_width:       Int = 23,
  val exp_width:       Int = 8,
  val ieee_compliance: Int = 1,
  val arch:            Int = 0,
  val extra_prec:      Int = 0)
    extends RawModule {
  // Create an instance of the BlackBox class
  protected val U1: CW_fp_ln = Module(new CW_fp_ln(sig_width, exp_width, ieee_compliance, arch, extra_prec))

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt((sig_width + exp_width + 1).W))
  })

  // Connect module ports to BlackBox ports
  U1.io.a   := io.a
  io.status := U1.io.status
  io.z      := U1.io.z
}

object fp_ln extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fp_ln") {
      def top = new fp_ln()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
