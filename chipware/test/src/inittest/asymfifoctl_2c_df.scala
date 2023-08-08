import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class asymfifoctl_2c_df(
  val data_s_width: Int = 16,
  val data_d_width: Int = 8,
  val ram_depth:    Int = 8,
  val mem_mode:     Int = 3,
  val arch_type:    Int = 1,
  val f_sync_type:  Int = 2,
  val r_sync_type:  Int = 2,
  val byte_order:   Int = 0,
  val flush_value:  Int = 0,
  val clk_ratio:    Int = 1,
  val ram_re_ext:   Int = 0,
  val err_mode:     Int = 0,
  val tst_mode:     Int = 0,
  val verif_en:     Int = 0)
    extends RawModule {
  protected val eff_depth: Int = mem_mode match {
    case 0 => ram_depth + 1
    case 1 => ram_depth + 2
    case 2 => ram_depth + 2
    case 3 => ram_depth + 3
    case 4 => ram_depth + 1
    case 5 => ram_depth + 2
    case 6 => ram_depth + 2
    case _ => ram_depth + 3
  }
  protected val BWBOmaxFIFOwrdCnt: Int = log2Ceil(eff_depth + 1)
  protected val BWBOmaxRAMaddr:    Int = log2Ceil(ram_depth)
  protected val BWBOmaxRAMwrdCnt:  Int = log2Ceil(ram_depth + 1)
  protected val width:             Int = if (data_s_width > data_d_width) data_s_width else data_d_width

  val io = IO(new Bundle {
    val rst_s_n:          Bool  = Input(Bool())
    val init_s_n:         Bool  = Input(Bool())
    val clk_s:            Clock = Input(Clock())
    val push_s_n:         Bool  = Input(Bool())
    val flush_s_n:        Bool  = Input(Bool())
    val data_s:           UInt  = Input(UInt(data_s_width.W))
    val rst_d_n:          Bool  = Input(Bool())
    val init_d_n:         Bool  = Input(Bool())
    val clk_d:            Clock = Input(Clock())
    val pop_d_n:          Bool  = Input(Bool())
    val data_d:           UInt  = Output(UInt(data_d_width.W))
    val wr_en_s_n:        Bool  = Output(Bool())
    val wr_addr_s:        UInt  = Output(UInt(BWBOmaxRAMaddr.W))
    val wr_data_s:        UInt  = Output(UInt(width.W))
    val ram_re_d_n:       Bool  = Output(Bool())
    val rd_addr_d:        UInt  = Output(UInt(BWBOmaxRAMaddr.W))
    val rd_data_d:        UInt  = Input(UInt(width.W))
    val clr_s:            Bool  = Input(Bool())
    val clr_sync_s:       Bool  = Output(Bool())
    val clr_in_prog_s:    Bool  = Output(Bool())
    val clr_cmplt_s:      Bool  = Output(Bool())
    val clr_d:            Bool  = Input(Bool())
    val clr_sync_d:       Bool  = Output(Bool())
    val clr_in_prog_d:    Bool  = Output(Bool())
    val clr_cmplt_d:      Bool  = Output(Bool())
    val ae_level_s:       UInt  = Input(UInt(BWBOmaxRAMwrdCnt.W))
    val af_level_s:       UInt  = Input(UInt(BWBOmaxRAMwrdCnt.W))
    val fifo_empty_s:     Bool  = Output(Bool())
    val empty_s:          Bool  = Output(Bool())
    val almost_empty_s:   Bool  = Output(Bool())
    val half_full_s:      Bool  = Output(Bool())
    val almost_full_s:    Bool  = Output(Bool())
    val full_s:           Bool  = Output(Bool())
    val inbuf_full_s:     Bool  = Output(Bool())
    val inbuf_part_wd_s:  UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W))
    val push_error_s:     Bool  = Output(Bool())
    val ram_word_cnt_s:   UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W))
    val fifo_word_cnt_s:  UInt  = Output(UInt(BWBOmaxFIFOwrdCnt.W))
    val ae_level_d:       UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W))
    val af_level_d:       UInt  = Input(UInt(BWBOmaxFIFOwrdCnt.W))
    val empty_d:          Bool  = Output(Bool())
    val almost_empty_d:   Bool  = Output(Bool())
    val half_full_d:      Bool  = Output(Bool())
    val almost_full_d:    Bool  = Output(Bool())
    val full_d:           Bool  = Output(Bool())
    val pop_error_d:      Bool  = Output(Bool())
    val outbuf_part_wd_d: UInt  = Output(UInt(BWBOmaxFIFOwrdCnt.W))
    val ram_word_cnt_d:   UInt  = Output(UInt(BWBOmaxRAMwrdCnt.W))
    val fifo_word_cnt_d:  UInt  = Output(UInt(BWBOmaxFIFOwrdCnt.W))
    val test:             Bool  = Input(Bool())
  })

  protected val U1: CW_asymfifoctl_2c_df = Module(
    new CW_asymfifoctl_2c_df(
      data_s_width,
      data_d_width,
      ram_depth,
      mem_mode,
      arch_type,
      f_sync_type,
      r_sync_type,
      byte_order,
      flush_value,
      clk_ratio,
      ram_re_ext,
      err_mode,
      tst_mode,
      verif_en
    )
  )

  U1.io.rst_s_n       := io.rst_s_n
  U1.io.init_s_n      := io.init_s_n
  U1.io.clk_s         := io.clk_s
  U1.io.push_s_n      := io.push_s_n
  U1.io.flush_s_n     := io.flush_s_n
  U1.io.data_s        := io.data_s
  U1.io.rst_d_n       := io.rst_d_n
  U1.io.init_d_n      := io.init_d_n
  U1.io.clk_d         := io.clk_d
  U1.io.pop_d_n       := io.pop_d_n
  io.data_d           := U1.io.data_d
  io.wr_en_s_n        := U1.io.wr_en_s_n
  io.wr_addr_s        := U1.io.wr_addr_s
  io.wr_data_s        := U1.io.wr_data_s
  io.ram_re_d_n       := U1.io.ram_re_d_n
  io.rd_addr_d        := U1.io.rd_addr_d
  U1.io.rd_data_d     := io.rd_data_d
  U1.io.clr_s         := io.clr_s
  io.clr_sync_s       := U1.io.clr_sync_s
  io.clr_in_prog_s    := U1.io.clr_in_prog_s
  io.clr_cmplt_s      := U1.io.clr_cmplt_s
  U1.io.clr_d         := io.clr_d
  io.clr_sync_d       := U1.io.clr_sync_d
  io.clr_in_prog_d    := U1.io.clr_in_prog_d
  io.clr_cmplt_d      := U1.io.clr_cmplt_d
  U1.io.ae_level_s    := io.ae_level_s
  U1.io.af_level_s    := io.af_level_s
  io.fifo_empty_s     := U1.io.fifo_empty_s
  io.empty_s          := U1.io.empty_s
  io.almost_empty_s   := U1.io.almost_empty_s
  io.half_full_s      := U1.io.half_full_s
  io.almost_full_s    := U1.io.almost_full_s
  io.full_s           := U1.io.full_s
  io.inbuf_full_s     := U1.io.inbuf_full_s
  io.inbuf_part_wd_s  := U1.io.inbuf_part_wd_s
  io.push_error_s     := U1.io.push_error_s
  io.ram_word_cnt_s   := U1.io.ram_word_cnt_s
  io.fifo_word_cnt_s  := U1.io.fifo_word_cnt_s
  U1.io.ae_level_d    := io.ae_level_d
  U1.io.af_level_d    := io.af_level_d
  io.empty_d          := U1.io.empty_d
  io.almost_empty_d   := U1.io.almost_empty_d
  io.half_full_d      := U1.io.half_full_d
  io.almost_full_d    := U1.io.almost_full_d
  io.full_d           := U1.io.full_d
  io.pop_error_d      := U1.io.pop_error_d
  io.outbuf_part_wd_d := U1.io.outbuf_part_wd_d
  io.ram_word_cnt_d   := U1.io.ram_word_cnt_d
  io.fifo_word_cnt_d  := U1.io.fifo_word_cnt_d
  U1.io.test          := io.test
}

object asymfifoctl_2c_df extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate asymfifoctl_2c_df") {
      def top = new asymfifoctl_2c_df(
        data_s_width = 16,
        data_d_width = 8,
        ram_depth    = 8,
        mem_mode     = 3,
        arch_type    = 1,
        f_sync_type  = 2,
        r_sync_type  = 2,
        byte_order   = 0,
        flush_value  = 0,
        clk_ratio    = 1,
        ram_re_ext   = 0,
        err_mode     = 0,
        tst_mode     = 0,
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
