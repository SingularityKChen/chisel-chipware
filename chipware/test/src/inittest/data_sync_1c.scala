import chisel3._
import circt.stage._
import utest._

class data_sync_1c(
  val width:       Int = 8,
  val f_sync_type: Int = 2,
  val filt_size:   Int = 1,
  val tst_mode:    Int = 0,
  val verif_en:    Int = 1)
    extends RawModule {
  val io = IO(new Bundle {
    val data_s:       UInt  = Input(UInt(width.W))
    val clk_d:        Clock = Input(Clock())
    val rst_d_n:      Bool  = Input(Bool())
    val init_d_n:     Bool  = Input(Bool())
    val filt_d:       UInt  = Input(UInt(filt_size.W))
    val test:         Bool  = Input(Bool())
    val data_avail_d: Bool  = Output(Bool())
    val data_d:       UInt  = Output(UInt(width.W))
    val max_skew_d:   UInt  = Output(UInt((filt_size + 1).W))
  })
  protected val U1: CW_data_sync_1c = Module(new CW_data_sync_1c(width, f_sync_type, filt_size, tst_mode, verif_en))
  U1.io.data_s    := io.data_s
  U1.io.clk_d     := io.clk_d
  U1.io.rst_d_n   := io.rst_d_n
  U1.io.init_d_n  := io.init_d_n
  U1.io.filt_d    := io.filt_d
  U1.io.test      := io.test
  io.data_avail_d := U1.io.data_avail_d
  io.data_d       := U1.io.data_d
  io.max_skew_d   := U1.io.max_skew_d
}

object data_sync_1c extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate data_sync_1c") {
      def top = new data_sync_1c(8, 2, 1, 0, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
