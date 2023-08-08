import chisel3._
import circt.stage._
import utest._

class stack(val width: Int = 8, val depth: Int = 4, val err_mode: Int = 0, val rst_mode: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val clk:        Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val push_req_n: Bool  = Input(Bool())
    val pop_req_n:  Bool  = Input(Bool())
    val data_in:    UInt  = Input(UInt(width.W))
    val empty:      Bool  = Output(Bool())
    val full:       Bool  = Output(Bool())
    val error:      Bool  = Output(Bool())
    val data_out:   UInt  = Output(UInt(width.W))
  })

  protected val U1: CW_stack = Module(new CW_stack(width, depth, err_mode, rst_mode))
  U1.io.clk        := io.clk
  U1.io.rst_n      := io.rst_n
  U1.io.push_req_n := io.push_req_n
  U1.io.pop_req_n  := io.pop_req_n
  U1.io.data_in    := io.data_in
  io.empty         := U1.io.empty
  io.full          := U1.io.full
  io.error         := U1.io.error
  io.data_out      := U1.io.data_out
}

object stack extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate stack") {
      def top = new stack(8, 4, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
