import chisel3._
import circt.stage._
import utest._

class sync(val width: Int = 8, val f_sync_type: Int = 2, val tst_mode: Int = 0, val verif_en: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val data_s:   UInt  = Input(UInt(width.W))
    val clk_d:    Clock = Input(Clock())
    val rst_d_n:  Bool  = Input(Bool())
    val init_d_n: Bool  = Input(Bool())
    val test:     Bool  = Input(Bool())
    val data_d:   UInt  = Output(UInt(width.W))
  })

  protected val U1: CW_sync = Module(new CW_sync(width, f_sync_type, tst_mode, verif_en))
  U1.io.data_s   := io.data_s
  U1.io.clk_d    := io.clk_d
  U1.io.rst_d_n  := io.rst_d_n
  U1.io.init_d_n := io.init_d_n
  U1.io.test     := io.test
  io.data_d      := U1.io.data_d
}

object sync extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate sync") {
      def top = new sync(8, 2, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
