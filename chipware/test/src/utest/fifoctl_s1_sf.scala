// filename: fifoctl_s1_sf.scala
import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class fifoctl_s1_sf(
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
    val we_n:         Bool  = Output(Bool())
    val empty:        Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
    val wr_addr:      UInt  = Output(UInt(log2Ceil(depth).W))
    val rd_addr:      UInt  = Output(UInt(log2Ceil(depth).W))
  })

  protected val U0: CW_fifoctl_s1_sf = Module(new CW_fifoctl_s1_sf(depth, ae_level, af_level, err_mode, rst_mode))
  U0.io.clk        := io.clk
  U0.io.rst_n      := io.rst_n
  U0.io.push_req_n := io.push_req_n
  U0.io.pop_req_n  := io.pop_req_n
  U0.io.diag_n     := io.diag_n
  io.we_n          := U0.io.we_n
  io.empty         := U0.io.empty
  io.almost_empty  := U0.io.almost_empty
  io.half_full     := U0.io.half_full
  io.almost_full   := U0.io.almost_full
  io.full          := U0.io.full
  io.error         := U0.io.error
  io.wr_addr       := U0.io.wr_addr
  io.rd_addr       := U0.io.rd_addr
}

object fifoctl_s1_sf extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fifoctl_s1_sf") {
      def top = new fifoctl_s1_sf()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
