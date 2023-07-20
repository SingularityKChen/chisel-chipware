import chisel3._
import circt.stage._
import utest._

class sqrt_seq(
  val width:       Int = 6,
  val tc_mode:     Int = 0,
  val num_cyc:     Int = 3,
  val rst_mode:    Int = 0,
  val input_mode:  Int = 1,
  val output_mode: Int = 1,
  val early_start: Int = 0)
    extends RawModule {
  protected val rootWidth: Int = (width + 1) / 2
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val rst_n:    Bool  = Input(Bool())
    val hold:     Bool  = Input(Bool())
    val start:    Bool  = Input(Bool())
    val a:        UInt  = Input(UInt(width.W))
    val complete: Bool  = Output(Bool())
    val root:     UInt  = Output(UInt(rootWidth.W))
  })

  // Define submodule
  protected val U1: CW_sqrt_seq = Module(
    new CW_sqrt_seq(width, tc_mode, num_cyc, rst_mode, input_mode, output_mode, early_start)
  )
  U1.io.clk   := io.clk
  U1.io.rst_n := io.rst_n
  U1.io.hold  := io.hold
  U1.io.start := io.start
  U1.io.a     := io.a
  io.complete := U1.io.complete
  io.root     := U1.io.root
}

object sqrt_seq extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate sqrt_seq") {
      def top = new sqrt_seq(6, 0, 3, 0, 1, 1, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
