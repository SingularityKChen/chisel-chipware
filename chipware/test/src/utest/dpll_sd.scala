import chisel3._
import chisel3.util.log2Ceil
import circt.stage._
import utest._

class dpll_sd(val width: Int = 1, val divisor: Int = 4, val gain: Int = 1, val filter: Int = 2, val windows: Int = 2)
    extends RawModule {
  val win_width: Int = log2Ceil(windows)
  val io = IO(new Bundle {
    val clk:       Clock = Input(Clock())
    val rst_n:     Bool  = Input(Bool())
    val stall:     Bool  = Input(Bool())
    val squelch:   Bool  = Input(Bool())
    val window:    UInt  = Input(UInt(win_width.W))
    val data_in:   UInt  = Input(UInt(width.W))
    val clk_out:   Clock = Output(Clock())
    val bit_ready: Bool  = Output(Bool())
    val data_out:  UInt  = Output(UInt(width.W))
  })
  // Instantiate the Chisel BlackBox
  protected val U1: CW_dpll_sd = Module(
    new CW_dpll_sd(
      width   = width,
      divisor = divisor,
      gain    = gain,
      filter  = filter,
      windows = windows
    )
  )

  // Connect the submodule's ports to the module's ports
  U1.io.clk     := io.clk
  U1.io.rst_n   := io.rst_n
  U1.io.stall   := io.stall
  U1.io.squelch := io.squelch
  U1.io.window  := io.window
  U1.io.data_in := io.data_in
  io.clk_out    := U1.io.clk_out
  io.bit_ready  := U1.io.bit_ready
  io.data_out   := U1.io.data_out
}

object dpll_sd extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate dpll_sd") {
      def top = new dpll_sd(1, 4, 1, 2, 2)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
