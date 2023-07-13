import chisel3._
import circt.stage._
import utest._

class sqrt_pipe(
  val width:       Int = 2,
  val tc_mode:     Int = 0,
  val num_stages:  Int = 2,
  val stall_mode:  Int = 1,
  val rst_mode:    Int = 1,
  val op_iso_mode: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:   Clock = Input(Clock())
    val rst_n: Bool  = Input(Bool())
    val en:    Bool  = Input(Bool())
    val a:     UInt  = Input(UInt(width.W))
    val root:  UInt  = Output(UInt(((width + 1) / 2).W))
  })

  protected val U1: CW_sqrt_pipe = Module(
    new CW_sqrt_pipe(width, tc_mode, num_stages, stall_mode, rst_mode, op_iso_mode)
  )
  U1.io.clk   := io.clk
  U1.io.rst_n := io.rst_n
  U1.io.en    := io.en
  U1.io.a     := io.a
  io.root     := U1.io.root
}

object sqrt_pipe extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate sqrt_pipe") {
      def top = new sqrt_pipe(2, 0, 2, 1, 1, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
