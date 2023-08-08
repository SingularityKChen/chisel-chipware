import chisel3._
import circt.stage._
import utest._

class gray_sync(
  val width:              Int = 8,
  val offset:             Int = 0,
  val reg_count_d:        Int = 1,
  val f_sync_type:        Int = 2,
  val tst_mode:           Int = 0,
  val verif_en:           Int = 0,
  val pipe_delay:         Int = 0,
  val reg_count_s:        Int = 1,
  val reg_offset_count_s: Int = 1)
    extends RawModule {
  val io = IO(new Bundle {
    val clk_s:          Clock = Input(Clock())
    val rst_s_n:        Bool  = Input(Bool())
    val init_s_n:       Bool  = Input(Bool())
    val en_s:           Bool  = Input(Bool())
    val count_s:        UInt  = Output(UInt(width.W))
    val offset_count_s: UInt  = Output(UInt(width.W))
    val clk_d:          Clock = Input(Clock())
    val rst_d_n:        Bool  = Input(Bool())
    val init_d_n:       Bool  = Input(Bool())
    val count_d:        UInt  = Output(UInt(width.W))
    val test:           Bool  = Input(Bool())
  })
  protected val U1: CW_gray_sync = Module(
    new CW_gray_sync(
      width,
      offset,
      reg_count_d,
      f_sync_type,
      tst_mode,
      verif_en,
      pipe_delay,
      reg_count_s,
      reg_offset_count_s
    )
  )
  U1.io.clk_s       := io.clk_s
  U1.io.rst_s_n     := io.rst_s_n
  U1.io.init_s_n    := io.init_s_n
  U1.io.en_s        := io.en_s
  io.count_s        := U1.io.count_s
  io.offset_count_s := U1.io.offset_count_s
  U1.io.clk_d       := io.clk_d
  U1.io.rst_d_n     := io.rst_d_n
  U1.io.init_d_n    := io.init_d_n
  io.count_d        := U1.io.count_d
  U1.io.test        := io.test
}

object gray_sync extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate gray_sync") {
      def top = new gray_sync(
        width              = 8,
        offset             = 0,
        reg_count_d        = 1,
        f_sync_type        = 2,
        tst_mode           = 0,
        verif_en           = 0,
        pipe_delay         = 0,
        reg_count_s        = 1,
        reg_offset_count_s = 1
      )

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
