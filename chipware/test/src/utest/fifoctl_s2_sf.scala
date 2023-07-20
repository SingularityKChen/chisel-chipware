import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class fifoctl_s2_sf(
  val depth:       Int = 8,
  val push_ae_lvl: Int = 2,
  val push_af_lvl: Int = 2,
  val pop_ae_lvl:  Int = 2,
  val pop_af_lvl:  Int = 2,
  val err_mode:    Int = 0,
  val push_sync:   Int = 2,
  val pop_sync:    Int = 2,
  val rst_mode:    Int = 0,
  val tst_mode:    Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk_push:        Clock = Input(Clock())
    val clk_pop:         Clock = Input(Clock())
    val rst_n:           Bool  = Input(Bool())
    val push_req_n:      Bool  = Input(Bool())
    val pop_req_n:       Bool  = Input(Bool())
    val test:            Bool  = Input(Bool())
    val we_n:            Bool  = Output(Bool())
    val push_empty:      Bool  = Output(Bool())
    val push_ae:         Bool  = Output(Bool())
    val push_hf:         Bool  = Output(Bool())
    val push_af:         Bool  = Output(Bool())
    val push_full:       Bool  = Output(Bool())
    val push_error:      Bool  = Output(Bool())
    val pop_empty:       Bool  = Output(Bool())
    val pop_ae:          Bool  = Output(Bool())
    val pop_hf:          Bool  = Output(Bool())
    val pop_af:          Bool  = Output(Bool())
    val pop_full:        Bool  = Output(Bool())
    val pop_error:       Bool  = Output(Bool())
    val wr_addr:         UInt  = Output(UInt(log2Ceil(depth).W))
    val rd_addr:         UInt  = Output(UInt(log2Ceil(depth).W))
    val push_word_count: UInt  = Output(UInt((log2Ceil(depth + 1)).W))
    val pop_word_count:  UInt  = Output(UInt(log2Ceil(depth).W))
  })

  protected val U1: CW_fifoctl_s2_sf = Module(
    new CW_fifoctl_s2_sf(
      depth,
      push_ae_lvl,
      push_af_lvl,
      pop_ae_lvl,
      pop_af_lvl,
      err_mode,
      push_sync,
      pop_sync,
      rst_mode,
      tst_mode
    )
  )
  U1.io.clk_push     := io.clk_push
  U1.io.clk_pop      := io.clk_pop
  U1.io.rst_n        := io.rst_n
  U1.io.push_req_n   := io.push_req_n
  U1.io.pop_req_n    := io.pop_req_n
  U1.io.test         := io.test
  io.we_n            := U1.io.we_n
  io.push_empty      := U1.io.push_empty
  io.push_ae         := U1.io.push_ae
  io.push_hf         := U1.io.push_hf
  io.push_af         := U1.io.push_af
  io.push_full       := U1.io.push_full
  io.push_error      := U1.io.push_error
  io.pop_empty       := U1.io.pop_empty
  io.pop_ae          := U1.io.pop_ae
  io.pop_hf          := U1.io.pop_hf
  io.pop_af          := U1.io.pop_af
  io.pop_full        := U1.io.pop_full
  io.pop_error       := U1.io.pop_error
  io.wr_addr         := U1.io.wr_addr
  io.rd_addr         := U1.io.rd_addr
  io.push_word_count := U1.io.push_word_count
  io.pop_word_count  := U1.io.pop_word_count
}

object fifoctl_s2_sf extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fifoctl_s2_sf") {
      def top = new fifoctl_s2_sf()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
