import chisel3._
import circt.stage._
import chisel3.util.log2Ceil
import utest._

class asymfifoctl_s2_sf(
  val data_in_width:  Int = 3,
  val data_out_width: Int = 12,
  val depth:          Int = 8,
  val push_ae_lvl:    Int = 2,
  val push_af_lvl:    Int = 2,
  val pop_ae_lvl:     Int = 2,
  val pop_af_lvl:     Int = 2,
  val err_mode:       Int = 0,
  val push_sync:      Int = 2,
  val pop_sync:       Int = 2,
  val rst_mode:       Int = 0,
  val byte_order:     Int = 0)
    extends RawModule {

  protected val max_data_width: Int = math.max(data_in_width, data_out_width)

  val io = IO(new Bundle {
    val clk_push:   Bool = Input(Bool())
    val clk_pop:    Bool = Input(Bool())
    val rst_n:      Bool = Input(Bool())
    val push_req_n: Bool = Input(Bool())
    val flush_n:    Bool = Input(Bool())
    val pop_req_n:  Bool = Input(Bool())
    val data_in:    UInt = Input(UInt(data_in_width.W))
    val rd_data:    UInt = Input(UInt(max_data_width.W))
    val we_n:       Bool = Output(Bool())
    val push_empty: Bool = Output(Bool())
    val push_ae:    Bool = Output(Bool())
    val push_hf:    Bool = Output(Bool())
    val push_af:    Bool = Output(Bool())
    val push_full:  Bool = Output(Bool())
    val ram_full:   Bool = Output(Bool())
    val part_wd:    Bool = Output(Bool())
    val push_error: Bool = Output(Bool())
    val pop_empty:  Bool = Output(Bool())
    val pop_ae:     Bool = Output(Bool())
    val pop_hf:     Bool = Output(Bool())
    val pop_af:     Bool = Output(Bool())
    val pop_full:   Bool = Output(Bool())
    val pop_error:  Bool = Output(Bool())
    val wr_data:    UInt = Output(UInt(max_data_width.W))
    val wr_addr:    UInt = Output(UInt(log2Ceil(depth).W))
    val rd_addr:    UInt = Output(UInt(log2Ceil(depth).W))
    val data_out:   UInt = Output(UInt(data_out_width.W))
  })

  protected val U1: CW_asymfifoctl_s2_sf = Module(
    new CW_asymfifoctl_s2_sf(
      data_in_width,
      data_out_width,
      depth,
      push_ae_lvl,
      push_af_lvl,
      pop_ae_lvl,
      pop_af_lvl,
      err_mode,
      push_sync,
      pop_sync,
      rst_mode,
      byte_order
    )
  )
  U1.io.clk_push   := io.clk_push
  U1.io.clk_pop    := io.clk_pop
  U1.io.rst_n      := io.rst_n
  U1.io.push_req_n := io.push_req_n
  U1.io.flush_n    := io.flush_n
  U1.io.pop_req_n  := io.pop_req_n
  U1.io.data_in    := io.data_in
  U1.io.rd_data    := io.rd_data
  io.we_n          := U1.io.we_n
  io.push_empty    := U1.io.push_empty
  io.push_ae       := U1.io.push_ae
  io.push_hf       := U1.io.push_hf
  io.push_af       := U1.io.push_af
  io.push_full     := U1.io.push_full
  io.ram_full      := U1.io.ram_full
  io.part_wd       := U1.io.part_wd
  io.push_error    := U1.io.push_error
  io.pop_empty     := U1.io.pop_empty
  io.pop_ae        := U1.io.pop_ae
  io.pop_hf        := U1.io.pop_hf
  io.pop_af        := U1.io.pop_af
  io.pop_full      := U1.io.pop_full
  io.pop_error     := U1.io.pop_error
  io.wr_data       := U1.io.wr_data
  io.wr_addr       := U1.io.wr_addr
  io.rd_addr       := U1.io.rd_addr
  io.data_out      := U1.io.data_out
}

object asymfifoctl_s2_sf extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate asymfifoctl_s2_sf") {
      def top = new asymfifoctl_s2_sf(3, 12, 8, 2, 2, 2, 2, 0, 2, 2, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
