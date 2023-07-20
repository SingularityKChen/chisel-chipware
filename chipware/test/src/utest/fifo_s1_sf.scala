import chisel3._
import circt.stage._
import utest._

class fifo_s1_sf(
  val width:    Int = 8,
  val depth:    Int = 4,
  val ae_level: Int = 1,
  val af_level: Int = 1,
  val err_mode: Int = 0,
  val rst_mode: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt(width.W))
    val empty:        Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
    val data_out:     UInt  = Output(UInt(width.W))
  })

  protected val U1: CW_fifo_s1_sf = Module(new CW_fifo_s1_sf(width, depth, ae_level, af_level, err_mode, rst_mode))
  U1.io.clk        := io.clk
  U1.io.rst_n      := io.rst_n
  U1.io.push_req_n := io.push_req_n
  U1.io.pop_req_n  := io.pop_req_n
  U1.io.diag_n     := io.diag_n
  U1.io.data_in    := io.data_in
  io.empty         := U1.io.empty
  io.almost_empty  := U1.io.almost_empty
  io.half_full     := U1.io.half_full
  io.almost_full   := U1.io.almost_full
  io.full          := U1.io.full
  io.error         := U1.io.error
  io.data_out      := U1.io.data_out
}

object fifo_s1_sf extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fifo_s1_sf") {
      def top = new fifo_s1_sf()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
