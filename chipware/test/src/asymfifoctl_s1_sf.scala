import chisel3._
import circt.stage._
import chisel3.util.log2Ceil
import utest._

class asymfifoctl_s1_sf(
  val data_in_width:  Int = 4,
  val data_out_width: Int = 16,
  val depth:          Int = 10,
  val ae_level:       Int = 1,
  val af_level:       Int = 9,
  val err_mode:       Int = 1,
  val rst_mode:       Int = 1,
  val byte_order:     Int = 0)
    extends RawModule {
  protected val log_depth:      Int = log2Ceil(depth)
  protected val max_data_width: Int = if (data_in_width > data_out_width) data_in_width else data_out_width

  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val flush_n:      Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt(data_in_width.W))
    val rd_data:      UInt  = Input(UInt(max_data_width.W))
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
    val wr_addr:      UInt  = Output(UInt(log_depth.W))
    val rd_addr:      UInt  = Output(UInt(log_depth.W))
    val data_out:     UInt  = Output(UInt(data_out_width.W))
  })

  protected val U0: CW_asymfifoctl_s1_sf = Module(
    new CW_asymfifoctl_s1_sf(
      data_in_width  = data_in_width,
      data_out_width = data_out_width,
      depth          = depth,
      ae_level       = ae_level,
      af_level       = af_level,
      err_mode       = err_mode,
      rst_mode       = rst_mode,
      byte_order     = byte_order
    )
  )

  U0.io.clk        := io.clk
  U0.io.rst_n      := io.rst_n
  U0.io.push_req_n := io.push_req_n
  U0.io.flush_n    := io.flush_n
  U0.io.pop_req_n  := io.pop_req_n
  U0.io.diag_n     := io.diag_n
  U0.io.data_in    := io.data_in
  U0.io.rd_data    := io.rd_data
  io.we_n          := U0.io.we_n
  io.empty         := U0.io.empty
  io.almost_empty  := U0.io.almost_empty
  io.half_full     := U0.io.half_full
  io.almost_full   := U0.io.almost_full
  io.full          := U0.io.full
  io.ram_full      := U0.io.ram_full
  io.error         := U0.io.error
  io.part_wd       := U0.io.part_wd
  io.wr_data       := U0.io.wr_data
  io.wr_addr       := U0.io.wr_addr
  io.rd_addr       := U0.io.rd_addr
  io.data_out      := U0.io.data_out
}

object asymfifoctl_s1_sf extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate asymfifoctl_s1_sf") {
      def top = new asymfifoctl_s1_sf(
        data_in_width  = 4,
        data_out_width = 16,
        depth          = 10,
        ae_level       = 1,
        af_level       = 9,
        err_mode       = 1,
        rst_mode       = 1,
        byte_order     = 0
      )

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
