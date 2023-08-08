import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class fifoctl_s1_df(val depth: Int = 4, val err_mode: Int = 0, val rst_mode: Int = 0) extends RawModule {
  protected val log_depth: Int = log2Ceil(depth)

  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val ae_level:     UInt  = Input(UInt(log_depth.W))
    val af_thresh:    UInt  = Input(UInt(log_depth.W))
    val empty:        Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val rd_addr:      UInt  = Output(UInt(log_depth.W))
    val wr_addr:      UInt  = Output(UInt(log_depth.W))
    val we_n:         Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
  })

  protected val U1: CW_fifoctl_s1_df = Module(new CW_fifoctl_s1_df(depth, err_mode, rst_mode))
  U1.io.clk        := io.clk
  U1.io.rst_n      := io.rst_n
  U1.io.push_req_n := io.push_req_n
  U1.io.pop_req_n  := io.pop_req_n
  U1.io.diag_n     := io.diag_n
  U1.io.ae_level   := io.ae_level
  U1.io.af_thresh  := io.af_thresh
  io.empty         := U1.io.empty
  io.full          := U1.io.full
  io.half_full     := U1.io.half_full
  io.almost_full   := U1.io.almost_full
  io.almost_empty  := U1.io.almost_empty
  io.rd_addr       := U1.io.rd_addr
  io.wr_addr       := U1.io.wr_addr
  io.we_n          := U1.io.we_n
  io.error         := U1.io.error
}

object fifoctl_s1_df extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fifoctl_s1_df") {
      def top = new fifoctl_s1_df()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
