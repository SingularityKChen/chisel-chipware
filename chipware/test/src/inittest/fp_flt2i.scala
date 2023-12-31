import chisel3._
import circt.stage._
import utest._

class fp_flt2i(val sig_width: Int = 23, val exp_width: Int = 8, val isize: Int = 32, val ieee_compliance: Int = 1)
    extends RawModule {
  // Define an instance of the BlackBox class
  protected val U1: CW_fp_flt2i = Module(new CW_fp_flt2i(sig_width, exp_width, isize, ieee_compliance))

  // Define ports matching the BlackBox class
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd:    UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt(isize.W))
  })

  // Connect the module ports to the BlackBox ports
  U1.io.a   := io.a
  U1.io.rnd := io.rnd
  io.status := U1.io.status
  io.z      := U1.io.z
}

object fp_flt2i extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fp_flt2i") {
      def top = new fp_flt2i()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
