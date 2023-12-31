import chisel3._
import circt.stage._
import utest._

class fp_cmp(val sig_width: Int = 23, val exp_width: Int = 8, val ieee_compliance: Int = 1, val quieten_nans: Int = 1)
    extends RawModule {
  val io = IO(new Bundle {
    val a:         UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b:         UInt = Input(UInt((sig_width + exp_width + 1).W))
    val status0:   UInt = Output(UInt(8.W))
    val status1:   UInt = Output(UInt(8.W))
    val z0:        UInt = Output(UInt((sig_width + exp_width + 1).W))
    val z1:        UInt = Output(UInt((sig_width + exp_width + 1).W))
    val zctr:      Bool = Input(Bool())
    val agtb:      Bool = Output(Bool())
    val altb:      Bool = Output(Bool())
    val aeqb:      Bool = Output(Bool())
    val unordered: Bool = Output(Bool())
  })

  // Instantiate the BlackBox
  protected val U1: CW_fp_cmp = Module(new CW_fp_cmp(sig_width, exp_width, ieee_compliance, quieten_nans))

  // Connect the ports
  U1.io.a      := io.a
  U1.io.b      := io.b
  io.status0   := U1.io.status0
  io.status1   := U1.io.status1
  io.z0        := U1.io.z0
  io.z1        := U1.io.z1
  U1.io.zctr   := io.zctr
  io.agtb      := U1.io.agtb
  io.altb      := U1.io.altb
  io.aeqb      := U1.io.aeqb
  io.unordered := U1.io.unordered
}

object fp_cmp extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fp_cmp") {
      def top = new fp_cmp()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
