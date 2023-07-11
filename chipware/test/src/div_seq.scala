import chisel3._
import circt.stage._
import utest._

class div_seq(
  val a_width:     Int = 3,
  val b_width:     Int = 3,
  val tc_mode:     Int = 0,
  val num_cyc:     Int = 3,
  val rst_mode:    Int = 0,
  val input_mode:  Int = 1,
  val output_mode: Int = 1,
  val early_start: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val hold:        Bool  = Input(Bool())
    val start:       Bool  = Input(Bool())
    val a:           UInt  = Input(UInt(a_width.W))
    val b:           UInt  = Input(UInt(b_width.W))
    val complete:    Bool  = Output(Bool())
    val divide_by_0: Bool  = Output(Bool())
    val quotient:    UInt  = Output(UInt(a_width.W))
    val remainder:   UInt  = Output(UInt(b_width.W))
  })

  protected val U1: CW_div_seq = Module(
    new CW_div_seq(a_width, b_width, tc_mode, num_cyc, rst_mode, input_mode, output_mode, early_start)
  )
  U1.io.clk      := io.clk
  U1.io.rst_n    := io.rst_n
  U1.io.hold     := io.hold
  U1.io.start    := io.start
  U1.io.a        := io.a
  U1.io.b        := io.b
  io.complete    := U1.io.complete
  io.divide_by_0 := U1.io.divide_by_0
  io.quotient    := U1.io.quotient
  io.remainder   := U1.io.remainder
}

object div_seq extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate div_seq") {
      def top = new div_seq(3, 3, 0, 3, 0, 1, 1, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
