import chisel3._
import circt.stage._
import utest._

class mult_pipe(
  val a_width:     Int = 2,
  val b_width:     Int = 2,
  val num_stages:  Int = 2,
  val stall_mode:  Int = 1,
  val rst_mode:    Int = 1,
  val op_iso_mode: Int = 0)
    extends RawModule {
  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:     Clock = Input(Clock())
    val rst_n:   Bool  = Input(Bool())
    val en:      Bool  = Input(Bool())
    val tc:      Bool  = Input(Bool())
    val a:       UInt  = Input(UInt(a_width.W))
    val b:       UInt  = Input(UInt(b_width.W))
    val product: UInt  = Output(UInt((a_width + b_width - 1).W))
  })

  // Define the submodule
  protected val U1: CW_mult_pipe = Module(
    new CW_mult_pipe(a_width, b_width, num_stages, stall_mode, rst_mode, op_iso_mode)
  )
  U1.io.clk   := io.clk
  U1.io.rst_n := io.rst_n
  U1.io.en    := io.en
  U1.io.tc    := io.tc
  U1.io.a     := io.a
  U1.io.b     := io.b
  io.product  := U1.io.product
}

object mult_pipe extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate mult_pipe") {
      def top = new mult_pipe()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
