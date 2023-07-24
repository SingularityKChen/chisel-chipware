import chisel3._
import circt.stage._
import utest._

class pulse_sync(
  reg_event:   Int = 1,
  f_sync_type: Int = 2,
  tst_mode:    Int = 0,
  verif_en:    Int = 0,
  pulse_mode:  Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val event_s:  Bool  = Input(Bool())
    val clk_s:    Clock = Input(Clock())
    val init_s_n: Bool  = Input(Bool())
    val rst_s_n:  Bool  = Input(Bool())
    val clk_d:    Clock = Input(Clock())
    val init_d_n: Bool  = Input(Bool())
    val rst_d_n:  Bool  = Input(Bool())
    val test:     Bool  = Input(Bool())
    val event_d:  Bool  = Output(Bool())
  })

  protected val U1: CW_pulse_sync = Module(new CW_pulse_sync(reg_event, f_sync_type, tst_mode, verif_en, pulse_mode))
  U1.io.event_s  := io.event_s
  U1.io.clk_s    := io.clk_s
  U1.io.init_s_n := io.init_s_n
  U1.io.rst_s_n  := io.rst_s_n
  U1.io.clk_d    := io.clk_d
  U1.io.init_d_n := io.init_d_n
  U1.io.rst_d_n  := io.rst_d_n
  U1.io.test     := io.test
  io.event_d     := U1.io.event_d
}

object pulse_sync extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate pulse_sync") {
      def top = new pulse_sync()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
