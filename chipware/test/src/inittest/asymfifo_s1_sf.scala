import chisel3._
import circt.stage._
import utest._

class asymfifo_s1_sf(
  val data_in_width:  Int = 4,
  val data_out_width: Int = 16,
  val depth:          Int = 10,
  val ae_level:       Int = 1,
  val af_level:       Int = 9,
  val err_mode:       Int = 2,
  val rst_mode:       Int = 1,
  val byte_order:     Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val flush_n:      Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt(data_in_width.W))
    val empty:        Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val ram_full:     Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
    val part_wd:      Bool  = Output(Bool())
    val data_out:     UInt  = Output(UInt(data_out_width.W))
  })

  protected val U1: CW_asymfifo_s1_sf = Module(
    new CW_asymfifo_s1_sf(
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

  U1.io.clk        := io.clk
  U1.io.rst_n      := io.rst_n
  U1.io.push_req_n := io.push_req_n
  U1.io.flush_n    := io.flush_n
  U1.io.pop_req_n  := io.pop_req_n
  U1.io.diag_n     := io.diag_n
  U1.io.data_in    := io.data_in
  io.empty         := U1.io.empty
  io.almost_empty  := U1.io.almost_empty
  io.half_full     := U1.io.half_full
  io.almost_full   := U1.io.almost_full
  io.full          := U1.io.full
  io.ram_full      := U1.io.ram_full
  io.error         := U1.io.error
  io.part_wd       := U1.io.part_wd
  io.data_out      := U1.io.data_out
}

object asymfifo_s1_sf extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate asymfifo_s1_sf") {
      def top = new asymfifo_s1_sf(
        data_in_width  = 4,
        data_out_width = 16,
        depth          = 10,
        ae_level       = 1,
        af_level       = 9,
        err_mode       = 2,
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
