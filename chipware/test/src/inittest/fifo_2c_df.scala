import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class fifo_2c_df(
  val width:           Int = 8,
  val ram_depth:       Int = 8,
  val mem_mode:        Int = 0,
  val f_sync_type:     Int = 2,
  val r_sync_type:     Int = 2,
  val clk_ratio:       Int = 1,
  val rst_mode:        Int = 0,
  val err_mode:        Int = 0,
  val tst_mode:        Int = 0,
  val verif_en:        Int = 0,
  val clr_dual_domain: Int = 1,
  val arch_type:       Int = 0)
    extends RawModule {

  protected val eff_depth: Int = mem_mode match {
    case 0 => ram_depth + 1
    case 1 => ram_depth + 2
    case 2 => ram_depth + 2
    case 3 => ram_depth + 3
    case 4 => ram_depth + 1
    case 5 => ram_depth + 2
    case 6 => ram_depth + 2
    case _ => throw new IllegalArgumentException(s"Invalid mem_mode: $mem_mode")
  }
  protected val BWBOmaxFIFOwrdCnt:    Int     = log2Ceil(eff_depth + 1)
  protected val BWBOmaxRAMaddr:       Int     = log2Ceil(ram_depth)
  protected val BWBOmaxRAMwrdCnt:     Int     = log2Ceil(ram_depth + 1)
  protected val maxPtlRAMdepth_r_w_s: Int     = 1 << BWBOmaxRAMaddr
  protected val NLlessThanMPD_r_w_s:  Int     = maxPtlRAMdepth_r_w_s - ram_depth
  protected val isPWRof2_r_w_s:       Boolean = NLlessThanMPD_r_w_s == 0
  protected val isODD_r_w_s:          Boolean = (ram_depth % 2) == 1

  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_s_n:         Bool  = Input(Bool()) // source asynchronous reset
    val init_s_n:        Bool  = Input(Bool()) // source synchronous reset
    val clk_s:           Clock = Input(Clock()) // source clock
    val push_s_n:        Bool  = Input(Bool()) // source (write) push requst
    val rst_d_n:         Bool  = Input(Bool()) // destination asynchronous reset
    val init_d_n:        Bool  = Input(Bool()) // destination synchronous reset
    val clk_d:           Clock = Input(Clock()) // destination clock
    val pop_d_n:         Bool  = Input(Bool()) // destination (read) pop request
    val data_s:          UInt  = Input(UInt(width.W)) // source push data
    val clr_s:           Bool  = Input(Bool()) // source initiated clear ram
    val clr_d:           Bool  = Input(Bool()) // destination initiated clear ram
    val ae_level_s:      UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W)) // source almost empty level
    val af_level_s:      UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W)) // source almost full level
    val ae_level_d:      UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W)) // destination fifo almost empty level
    val af_level_d:      UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W)) // destination fifo almost full level
    val test:            Bool  = Input(Bool()) // scan test mode select used if parameter tst_mode is 1 or 2
    val data_d:          UInt  = Output(UInt(width.W)) // destination pop data
    val clr_sync_s:      Bool  = Output(Bool()) // source initiated coordinated clear
    val clr_in_prog_s:   Bool  = Output(Bool()) // source initiated clear in progress
    val clr_cmplt_s:     Bool  = Output(Bool()) // source clear complete status pulse
    val clr_sync_d:      Bool  = Output(Bool()) // destination initiated coordinated clear
    val clr_in_prog_d:   Bool  = Output(Bool()) // destination initiated clear in progress
    val clr_cmplt_d:     Bool  = Output(Bool()) // destination clear complete status pulse
    val fifo_empty_s:    Bool  = Output(Bool()) // source fifo (ram + cache) empty flag
    val empty_s:         Bool  = Output(Bool()) // source ram empty flag
    val almost_empty_s:  Bool  = Output(Bool()) // source ram almost empty flag
    val half_full_s:     Bool  = Output(Bool()) // source ram half full flag
    val almost_full_s:   Bool  = Output(Bool()) // source ram almost full flag
    val full_s:          Bool  = Output(Bool()) // source ram full flag
    val error_s:         Bool  = Output(Bool()) // source push error flag
    val word_cnt_s:      UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W)) // source ram word count
    val fifo_word_cnt_s: UInt  = Output(UInt(BWBOmaxFIFOwrdCnt.W)) // source fifo word count
    val empty_d:         Bool  = Output(Bool()) // destination fifo empty flag
    val almost_empty_d:  Bool  = Output(Bool()) // destination fifo almost empty flag
    val half_full_d:     Bool  = Output(Bool()) // destination fifo half full flag
    val almost_full_d:   Bool  = Output(Bool()) // destination fifo almost full flag
    val full_d:          Bool  = Output(Bool()) // destination fifo full flag
    val error_d:         Bool  = Output(Bool()) // destination pop error flag
    val word_cnt_d:      UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W)) // destination ram word count
  })
  protected val U1 = Module(
    new CW_fifo_2c_df(
      width,
      ram_depth,
      mem_mode,
      f_sync_type,
      r_sync_type,
      clk_ratio,
      rst_mode,
      err_mode,
      tst_mode,
      verif_en,
      clr_dual_domain,
      arch_type
    )
  )
  U1.io.rst_s_n      := io.rst_s_n
  U1.io.init_s_n     := io.init_s_n
  U1.io.clk_s        := io.clk_s
  U1.io.push_s_n     := io.push_s_n
  U1.io.rst_d_n      := io.rst_d_n
  U1.io.init_d_n     := io.init_d_n
  U1.io.clk_d        := io.clk_d
  U1.io.pop_d_n      := io.pop_d_n
  U1.io.data_s       := io.data_s
  U1.io.clr_s        := io.clr_s
  U1.io.clr_d        := io.clr_d
  U1.io.ae_level_s   := io.ae_level_s
  U1.io.af_level_s   := io.af_level_s
  U1.io.ae_level_d   := io.ae_level_d
  U1.io.af_level_d   := io.af_level_d
  U1.io.test         := io.test
  io.data_d          := U1.io.data_d
  io.clr_sync_s      := U1.io.clr_sync_s
  io.clr_in_prog_s   := U1.io.clr_in_prog_s
  io.clr_cmplt_s     := U1.io.clr_cmplt_s
  io.clr_sync_d      := U1.io.clr_sync_d
  io.clr_in_prog_d   := U1.io.clr_in_prog_d
  io.clr_cmplt_d     := U1.io.clr_cmplt_d
  io.fifo_empty_s    := U1.io.fifo_empty_s
  io.empty_s         := U1.io.empty_s
  io.almost_empty_s  := U1.io.almost_empty_s
  io.half_full_s     := U1.io.half_full_s
  io.almost_full_s   := U1.io.almost_full_s
  io.full_s          := U1.io.full_s
  io.error_s         := U1.io.error_s
  io.word_cnt_s      := U1.io.word_cnt_s
  io.fifo_word_cnt_s := U1.io.fifo_word_cnt_s
  io.empty_d         := U1.io.empty_d
  io.almost_empty_d  := U1.io.almost_empty_d
  io.half_full_d     := U1.io.half_full_d
  io.almost_full_d   := U1.io.almost_full_d
  io.full_d          := U1.io.full_d
  io.error_d         := U1.io.error_d
  io.word_cnt_d      := U1.io.word_cnt_d
}

object fifo_2c_df extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fifo_2c_df") {
      def top = new fifo_2c_df()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
