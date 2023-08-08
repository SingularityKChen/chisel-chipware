import chisel3._
import circt.stage._
import utest._

class div_sat(
  val a_width:  Int = 8,
  val b_width:  Int = 8,
  val q_width:  Int = 8,
  val tc_mode:  Int = 0,
  val rem_mode: Int = 1)
    extends RawModule {
  val io = IO(new Bundle {
    val a:           UInt = Input(UInt(a_width.W))
    val b:           UInt = Input(UInt(b_width.W))
    val quotient:    UInt = Output(UInt(q_width.W))
    val remainder:   UInt = Output(UInt(b_width.W))
    val divide_by_0: Bool = Output(Bool())
    val saturation:  Bool = Output(Bool())
  })

  protected val U1: CW_div_sat = Module(new CW_div_sat(a_width, b_width, q_width, tc_mode, rem_mode))
  U1.io.a        := io.a
  U1.io.b        := io.b
  io.quotient    := U1.io.quotient
  io.remainder   := U1.io.remainder
  io.divide_by_0 := U1.io.divide_by_0
  io.saturation  := U1.io.saturation
}

object div_sat extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate div_sat") {
      def top = new div_sat(8, 8, 8, 0, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
