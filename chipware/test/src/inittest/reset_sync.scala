import chisel3._
import circt.stage._
import utest._

class reset_sync(
  val f_sync_type:  Int = 2,
  val r_sync_type:  Int = 2,
  val clk_d_faster: Int = 1,
  val reg_in_prog:  Int = 1,
  val tst_mode:     Int = 1,
  val verif_en:     Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk_s:         Clock = Input(Clock()) // source domain clock source
    val rst_s_n:       Bool  = Input(Bool()) // source domain asynchronous reset (low)
    val init_s_n:      Bool  = Input(Bool()) // source domain synchronous reset (low)
    val clr_s:         Bool  = Input(Bool()) // source domain clear
    val clk_d:         Clock = Input(Clock()) // destination domain clock source
    val rst_d_n:       Bool  = Input(Bool()) // destination domain asynchronous reset
    val init_d_n:      Bool  = Input(Bool()) // destination domain synchronous reset
    val clr_d:         Bool  = Input(Bool()) // destination domain clear
    val test:          Bool  = Input(Bool()) // scan test mode select input
    val clr_sync_s:    Bool  = Output(Bool()) // source domain clear for sequential logic
    val clr_in_prog_s: Bool  = Output(Bool()) // source domain clear sequence in progress
    val clr_cmplt_s:   Bool  = Output(Bool()) // source domain clear sequence complete
    val clr_in_prog_d: Bool  = Output(Bool()) // destination domain clear sequence in progress
    val clr_sync_d:    Bool  = Output(Bool()) // destination domain clear for sequential logic
    val clr_cmplt_d:   Bool  = Output(Bool()) // destination domain clear sequence complete
  })

  protected val U1: CW_reset_sync = Module(
    new CW_reset_sync(
      f_sync_type,
      r_sync_type,
      clk_d_faster,
      reg_in_prog,
      tst_mode,
      verif_en
    )
  )

  U1.io.clk_s      := io.clk_s
  U1.io.rst_s_n    := io.rst_s_n
  U1.io.init_s_n   := io.init_s_n
  U1.io.clr_s      := io.clr_s
  U1.io.clk_d      := io.clk_d
  U1.io.rst_d_n    := io.rst_d_n
  U1.io.init_d_n   := io.init_d_n
  U1.io.clr_d      := io.clr_d
  U1.io.test       := io.test
  io.clr_sync_s    := U1.io.clr_sync_s
  io.clr_in_prog_s := U1.io.clr_in_prog_s
  io.clr_cmplt_s   := U1.io.clr_cmplt_s
  io.clr_in_prog_d := U1.io.clr_in_prog_d
  io.clr_sync_d    := U1.io.clr_sync_d
  io.clr_cmplt_d   := U1.io.clr_cmplt_d
}

object reset_sync extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate reset_sync") {
      def top = new reset_sync(
        f_sync_type  = 2,
        r_sync_type  = 2,
        clk_d_faster = 1,
        reg_in_prog  = 1,
        tst_mode     = 1,
        verif_en     = 0
      )

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
