import chisel3._
import circt.stage._
import utest._

class prod_sum_pipe(
  a_width:     Int = 2,
  b_width:     Int = 2,
  num_inputs:  Int = 2,
  sum_width:   Int = 4,
  num_stages:  Int = 2,
  stall_mode:  Int = 1,
  rst_mode:    Int = 1,
  op_iso_mode: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:   Clock = Input(Clock())
    val rst_n: Bool  = Input(Bool())
    val en:    Bool  = Input(Bool())
    val tc:    Bool  = Input(Bool())
    val a:     UInt  = Input(UInt((a_width * num_inputs).W))
    val b:     UInt  = Input(UInt((b_width * num_inputs).W))
    val sum:   UInt  = Output(UInt(sum_width.W))
  })

  protected val U1: CW_prod_sum_pipe = Module(
    new CW_prod_sum_pipe(a_width, b_width, num_inputs, sum_width, num_stages, stall_mode, rst_mode, op_iso_mode)
  )
  U1.io.clk   := io.clk
  U1.io.rst_n := io.rst_n
  U1.io.en    := io.en
  U1.io.tc    := io.tc
  U1.io.a     := io.a
  U1.io.b     := io.b
  io.sum      := U1.io.sum
}

object prod_sum_pipe extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate prod_sum_pipe") {
      def top = new prod_sum_pipe(2, 2, 2, 4, 2, 1, 1, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
