import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class fifo_s1_df(val width: Int = 8, val depth: Int = 4, val err_mode: Int = 0, val rst_mode: Int = 0)
    extends RawModule {

  protected val log_depth: Int = log2Ceil(depth)

  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock()) // clocks are defined by `Clock` type
    val rst_n:        Bool  = Input(Bool()) // reset input, active low
    val push_req_n:   Bool  = Input(Bool()) // FIFO push request, active low
    val pop_req_n:    Bool  = Input(Bool()) // FIFO pop request, active low
    val diag_n:       Bool  = Input(Bool()) // Diagnostic control, active low
    val ae_level:     UInt  = Input(UInt(log_depth.W)) // Almost empty level
    val af_thresh:    UInt  = Input(UInt(log_depth.W)) // Almost full threshold
    val data_in:      UInt  = Input(UInt(width.W)) // FIFO data to push
    val empty:        Bool  = Output(Bool()) // FIFO empty output, active high
    val almost_empty: Bool  = Output(Bool()) // FIFO almost empty output, active high
    val half_full:    Bool  = Output(Bool()) // FIFO half full output, active high
    val almost_full:  Bool  = Output(Bool()) // FIFO almost full output, active high
    val full:         Bool  = Output(Bool()) // FIFO full output, active high
    val error:        Bool  = Output(Bool()) // FIFO error output, active high
    val data_out:     UInt  = Output(UInt(width.W)) // FIFO data to pop
  })

  protected val U1: CW_fifo_s1_df = Module(new CW_fifo_s1_df(width, depth, err_mode, rst_mode))

  U1.io.clk        := io.clk
  U1.io.rst_n      := io.rst_n
  U1.io.push_req_n := io.push_req_n
  U1.io.pop_req_n  := io.pop_req_n
  U1.io.diag_n     := io.diag_n
  U1.io.ae_level   := io.ae_level
  U1.io.af_thresh  := io.af_thresh
  U1.io.data_in    := io.data_in
  io.empty         := U1.io.empty
  io.almost_empty  := U1.io.almost_empty
  io.half_full     := U1.io.half_full
  io.almost_full   := U1.io.almost_full
  io.full          := U1.io.full
  io.error         := U1.io.error
  io.data_out      := U1.io.data_out
}

object fifo_s1_df extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fifo_s1_df") {
      def top = new fifo_s1_df()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
