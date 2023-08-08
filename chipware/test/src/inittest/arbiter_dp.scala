import chisel3._
import chisel3.util.log2Ceil
import circt.stage._
import utest._

// Define a parameterized Chisel Module
class arbiter_dp(val n: Int, val park_mode: Int, val park_index: Int, val output_mode: Int) extends RawModule {
  // Declare the parameters
  protected val index_width: Int = log2Ceil(n)
  val io = IO(new Bundle {
    // Declare the ports
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val init_n:      Bool  = Input(Bool())
    val enable:      Bool  = Input(Bool())
    val request:     UInt  = Input(UInt(n.W))
    val lock:        UInt  = Input(UInt(n.W))
    val mask:        UInt  = Input(UInt(n.W))
    val prior:       UInt  = Input(UInt((n * index_width).W))
    val parked:      Bool  = Output(Bool())
    val granted:     Bool  = Output(Bool())
    val locked:      Bool  = Output(Bool())
    val grant:       UInt  = Output(UInt(n.W))
    val grant_index: UInt  = Output(UInt(index_width.W))
  })

  // Instantiate the Chisel BlackBox class
  protected val U1: CW_arbiter_dp = Module(new CW_arbiter_dp(n, park_mode, park_index, output_mode))

  // Connect the ports between the module and the BlackBox instance
  U1.io.clk      := io.clk
  U1.io.rst_n    := io.rst_n
  U1.io.init_n   := io.init_n
  U1.io.enable   := io.enable
  U1.io.request  := io.request
  U1.io.lock     := io.lock
  U1.io.mask     := io.mask
  U1.io.prior    := io.prior
  io.parked      := U1.io.parked
  io.granted     := U1.io.granted
  io.locked      := U1.io.locked
  io.grant       := U1.io.grant
  io.grant_index := U1.io.grant_index
}

object arbiter_dp extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate arbiter_dp") {
      def top = new arbiter_dp(2, 0, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
