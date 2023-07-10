import chisel3._
import circt.stage._
import chisel3.util.log2Ceil
import utest._

class asymfifoctl_s1_df(
  val data_in_width:  Int = 4,
  val data_out_width: Int = 16,
  val depth:          Int = 10,
  val err_mode:       Int = 2,
  val rst_mode:       Int = 1,
  val byte_order:     Int = 0)
    extends RawModule {
  protected val max_data_width: Int = math.max(data_in_width, data_out_width)
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val flush_n:      Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt(data_in_width.W))
    val rd_data:      UInt  = Input(UInt(max_data_width.W))
    val ae_level:     UInt  = Input(UInt(log2Ceil(depth).W))
    val af_thresh:    UInt  = Input(UInt(log2Ceil(depth).W))
    val we_n:         Bool  = Output(Bool())
    val empty:        Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val ram_full:     Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
    val part_wd:      Bool  = Output(Bool())
    val wr_data:      UInt  = Output(UInt(max_data_width.W))
    val wr_addr:      UInt  = Output(UInt(log2Ceil(depth).W))
    val rd_addr:      UInt  = Output(UInt(log2Ceil(depth).W))
    val data_out:     UInt  = Output(UInt(data_out_width.W))
  })

  protected val U1: CW_asymfifoctl_s1_df = Module(
    new CW_asymfifoctl_s1_df(data_in_width, data_out_width, depth, err_mode, rst_mode, byte_order)
  )
  U1.io.clk        := io.clk
  U1.io.rst_n      := io.rst_n
  U1.io.push_req_n := io.push_req_n
  U1.io.flush_n    := io.flush_n
  U1.io.pop_req_n  := io.pop_req_n
  U1.io.diag_n     := io.diag_n
  U1.io.data_in    := io.data_in
  U1.io.rd_data    := io.rd_data
  U1.io.ae_level   := io.ae_level
  U1.io.af_thresh  := io.af_thresh
  io.we_n          := U1.io.we_n
  io.empty         := U1.io.empty
  io.almost_empty  := U1.io.almost_empty
  io.half_full     := U1.io.half_full
  io.almost_full   := U1.io.almost_full
  io.full          := U1.io.full
  io.ram_full      := U1.io.ram_full
  io.error         := U1.io.error
  io.part_wd       := U1.io.part_wd
  io.wr_data       := U1.io.wr_data
  io.wr_addr       := U1.io.wr_addr
  io.rd_addr       := U1.io.rd_addr
  io.data_out      := U1.io.data_out
}

object asymfifoctl_s1_df extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate asymfifoctl_s1_df") {
      def top = new asymfifoctl_s1_df(4, 16, 10, 2, 1, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
