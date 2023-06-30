import chisel3._
import chisel3.util.log2Ceil
import circt.stage._
import utest._

class arbiter_2t(n: Int, p_width: Int, park_mode: Int, park_index: Int, output_mode: Int) extends RawModule {
  val io = IO(new Bundle {
    // Define the module's ports here
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val request:     UInt  = Input(UInt(n.W))
    val priority:    UInt  = Input(UInt((n * p_width).W))
    val lock:        UInt  = Input(UInt(n.W))
    val mask:        UInt  = Input(UInt(n.W))
    val parked:      Bool  = Output(Bool())
    val granted:     Bool  = Output(Bool())
    val locked:      Bool  = Output(Bool())
    val grant:       UInt  = Output(UInt(n.W))
    val grant_index: UInt  = Output(UInt(log2Ceil(n).W))
  })

  // Instantiate the Chisel BlackBox
  protected val U1: CW_arbiter_2t = Module(new CW_arbiter_2t(n, p_width, park_mode, park_index, output_mode))

  // Connect the submodule's ports to the module's ports
  U1.io.clk      := io.clk
  U1.io.rst_n    := io.rst_n
  U1.io.request  := io.request
  U1.io.priority := io.priority
  U1.io.lock     := io.lock
  U1.io.mask     := io.mask
  io.parked      := U1.io.parked
  io.granted     := U1.io.granted
  io.locked      := U1.io.locked
  io.grant       := U1.io.grant
  io.grant_index := U1.io.grant_index
}

object arbiter_2t extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate arbiter_2t") {
      def top = new arbiter_2t(2, 1, 0, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
