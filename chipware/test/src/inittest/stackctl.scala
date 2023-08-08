import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class stackctl(val depth: Int = 8, val err_mode: Int = 0, val rst_mode: Int = 0) extends RawModule {
  val log_depth: Int = log2Ceil(depth)

  val io = IO(new Bundle {
    val clk:        Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val push_req_n: Bool  = Input(Bool())
    val pop_req_n:  Bool  = Input(Bool())
    val we_n:       Bool  = Output(Bool())
    val empty:      Bool  = Output(Bool())
    val full:       Bool  = Output(Bool())
    val error:      Bool  = Output(Bool())
    val wr_addr:    UInt  = Output(UInt(log_depth.W))
    val rd_addr:    UInt  = Output(UInt(log_depth.W))
  })

  protected val U1: CW_stackctl = Module(new CW_stackctl(depth, err_mode, rst_mode))
  U1.io.clk        := io.clk
  U1.io.rst_n      := io.rst_n
  U1.io.push_req_n := io.push_req_n
  U1.io.pop_req_n  := io.pop_req_n
  io.we_n          := U1.io.we_n
  io.empty         := U1.io.empty
  io.full          := U1.io.full
  io.error         := U1.io.error
  io.wr_addr       := U1.io.wr_addr
  io.rd_addr       := U1.io.rd_addr
}

object stackctl extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate stackctl") {
      def top = new stackctl(8, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
