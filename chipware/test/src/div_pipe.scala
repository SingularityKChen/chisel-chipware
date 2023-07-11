import chisel3._
import circt.stage._
import utest._

class div_pipe(
  val a_width:     Int = 2,
  val b_width:     Int = 2,
  val tc_mode:     Int = 0,
  val rem_mode:    Int = 1,
  val num_stages:  Int = 2,
  val stall_mode:  Int = 1,
  val rst_mode:    Int = 1,
  val op_iso_mode: Int = 0,
  val arch:        Int = 1)
    extends RawModule {
  protected val bit_width_a: Int = a_width
  protected val bit_width_b: Int = b_width
  val io = IO(new Bundle {
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val en:          Bool  = Input(Bool())
    val a:           UInt  = Input(UInt(a_width.W))
    val b:           UInt  = Input(UInt(b_width.W))
    val quotient:    UInt  = Output(UInt(a_width.W))
    val remainder:   UInt  = Output(UInt(b_width.W))
    val divide_by_0: Bool  = Output(Bool())
  })

  protected val U1: CW_div_pipe = Module(
    new CW_div_pipe(a_width, b_width, tc_mode, rem_mode, num_stages, stall_mode, rst_mode, op_iso_mode, arch)
  )
  U1.io.clk      := io.clk
  U1.io.rst_n    := io.rst_n
  U1.io.en       := io.en
  U1.io.a        := io.a
  U1.io.b        := io.b
  io.quotient    := U1.io.quotient
  io.remainder   := U1.io.remainder
  io.divide_by_0 := U1.io.divide_by_0
}

object div_pipe extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate div_pipe") {
      def top = new div_pipe(2, 2, 0, 1, 2, 1, 1, 0, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
