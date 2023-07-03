import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class fifoctl_2c_df(
  val width:           Int = 8,
  val ram_depth:       Int = 8,
  val mem_mode:        Int = 0,
  val f_sync_type:     Int = 2,
  val r_sync_type:     Int = 2,
  val clk_ratio:       Int = 1,
  val ram_re_ext:      Int = 0,
  val err_mode:        Int = 0,
  val tst_mode:        Int = 0,
  val verif_en:        Int = 0,
  val clr_dual_domain: Int = 1,
  val arch_type:       Int = 0)
    extends RawModule {
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
    val data_d:          UInt  = Output(UInt(width.W)) // destination pop data
    val wr_en_s_n:       Bool  = Output(Bool()) // source write enable to ram
    val wr_addr_s:       UInt  = Output(UInt(log2Ceil(ram_depth).W)) // source write address to ram
    val ram_re_d_n:      Bool  = Output(Bool()) // destination read enable to ram
    val rd_addr_d:       UInt  = Output(UInt(log2Ceil(ram_depth).W)) // destination read address to ram
    val rd_data_d:       UInt  = Input(UInt(width.W)) // destination data from fifo
    val clr_s:           Bool  = Input(Bool()) // source initiated clear ram
    val clr_sync_s:      Bool  = Output(Bool()) // source coordinated clear
    val clr_in_prog_s:   Bool  = Output(Bool()) // source clear in progress
    val clr_cmplt_s:     Bool  = Output(Bool()) // source clear complete status pulse
    val clr_d:           Bool  = Input(Bool()) // destination initiated clear ram
    val clr_sync_d:      Bool  = Output(Bool()) // destination coordinated clear
    val clr_in_prog_d:   Bool  = Output(Bool()) // destination clear in progress
    val clr_cmplt_d:     Bool  = Output(Bool()) // destination clear complete status pulse
    val ae_level_s:      UInt  = Input(UInt(log2Ceil(ram_depth + 1).W)) // source almost empty level
    val af_level_s:      UInt  = Input(UInt(log2Ceil(ram_depth + 1).W)) // source almost full level
    val fifo_empty_s:    Bool  = Output(Bool()) // source fifo (ram + cache) empty flag
    val empty_s:         Bool  = Output(Bool()) // source ram empty flag
    val almost_empty_s:  Bool  = Output(Bool()) // source ram almost empty flag
    val half_full_s:     Bool  = Output(Bool()) // source ram half full flag
    val almost_full_s:   Bool  = Output(Bool()) // source ram almost full flag
    val full_s:          Bool  = Output(Bool()) // source ram full flag
    val error_s:         Bool  = Output(Bool()) // source push error flag
    val ram_word_cnt_s:  UInt  = Output(UInt(log2Ceil(ram_depth + 1).W)) // source ram word count
    val fifo_word_cnt_s: UInt  = Output(UInt(log2Ceil(ram_depth + 1).W)) // source fifo word count
    val ae_level_d:      UInt  = Input(UInt(log2Ceil(ram_depth + 1).W)) // destination almost empty level
    val af_level_d:      UInt  = Input(UInt(log2Ceil(ram_depth + 1).W)) // destination almost full level
    val empty_d:         Bool  = Output(Bool()) // destination fifo empty flag
    val almost_empty_d:  Bool  = Output(Bool()) // destination fifo almost empty flag
    val half_full_d:     Bool  = Output(Bool()) // destination fifo half full flag
    val almost_full_d:   Bool  = Output(Bool()) // destination fifo almost full flag
    val full_d:          Bool  = Output(Bool()) // destination fifo full flag
    val error_d:         Bool  = Output(Bool()) // destination pop  error flag
    val ram_word_cnt_d:  UInt  = Output(UInt(log2Ceil(ram_depth + 1).W)) // destination ram  word count
    val fifo_word_cnt_d: UInt  = Output(UInt(log2Ceil(ram_depth + 1).W)) // destination fifo_word count
    val test:            Bool  = Input(Bool()) // scan test mode select
  })

  // Instantiate the previous Chisel BlackBox class
  protected val U1: CW_fifoctl_2c_df = Module(
    new CW_fifoctl_2c_df(
      width,
      ram_depth,
      mem_mode,
      f_sync_type,
      r_sync_type,
      clk_ratio,
      ram_re_ext,
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
  io.data_d          := U1.io.data_d
  io.wr_en_s_n       := U1.io.wr_en_s_n
  io.wr_addr_s       := U1.io.wr_addr_s
  io.ram_re_d_n      := U1.io.ram_re_d_n
  io.rd_addr_d       := U1.io.rd_addr_d
  U1.io.rd_data_d    := io.rd_data_d
  U1.io.clr_s        := io.clr_s
  io.clr_sync_s      := U1.io.clr_sync_s
  io.clr_in_prog_s   := U1.io.clr_in_prog_s
  io.clr_cmplt_s     := U1.io.clr_cmplt_s
  U1.io.clr_d        := io.clr_d
  io.clr_sync_d      := U1.io.clr_sync_d
  io.clr_in_prog_d   := U1.io.clr_in_prog_d
  io.clr_cmplt_d     := U1.io.clr_cmplt_d
  U1.io.ae_level_s   := io.ae_level_s
  U1.io.af_level_s   := io.af_level_s
  io.fifo_empty_s    := U1.io.fifo_empty_s
  io.empty_s         := U1.io.empty_s
  io.almost_empty_s  := U1.io.almost_empty_s
  io.half_full_s     := U1.io.half_full_s
  io.almost_full_s   := U1.io.almost_full_s
  io.full_s          := U1.io.full_s
  io.error_s         := U1.io.error_s
  io.ram_word_cnt_s  := U1.io.ram_word_cnt_s
  io.fifo_word_cnt_s := U1.io.fifo_word_cnt_s
  U1.io.ae_level_d   := io.ae_level_d
  U1.io.af_level_d   := io.af_level_d
  io.empty_d         := U1.io.empty_d
  io.almost_empty_d  := U1.io.almost_empty_d
  io.half_full_d     := U1.io.half_full_d
  io.almost_full_d   := U1.io.almost_full_d
  io.full_d          := U1.io.full_d
  io.error_d         := U1.io.error_d
  io.ram_word_cnt_d  := U1.io.ram_word_cnt_d
  io.fifo_word_cnt_d := U1.io.fifo_word_cnt_d
  U1.io.test         := io.test
}

object fifoctl_2c_df extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fifoctl_2c_df") {
      def top = new fifoctl_2c_df()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
