import chisel3._
import circt.stage._
import utest._

class asymdata_outbuf(val in_width: Int = 16, val out_width: Int = 8, val err_mode: Int = 0, val byte_order: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk_pop:    Clock = Input(Clock())
    val rst_pop_n:  Bool  = Input(Bool())
    val init_pop_n: Bool  = Input(Bool())
    val pop_req_n:  Bool  = Input(Bool())
    val data_in:    UInt  = Input(UInt(in_width.W))
    val fifo_empty: Bool  = Input(Bool())
    val pop_wd_n:   Bool  = Output(Bool())
    val data_out:   UInt  = Output(UInt(out_width.W))
    val part_wd:    Bool  = Output(Bool())
    val pop_error:  Bool  = Output(Bool())
  })

  protected val U1: CW_asymdata_outbuf = Module(new CW_asymdata_outbuf(in_width, out_width, err_mode, byte_order))
  U1.io.clk_pop    := io.clk_pop
  U1.io.rst_pop_n  := io.rst_pop_n
  U1.io.init_pop_n := io.init_pop_n
  U1.io.pop_req_n  := io.pop_req_n
  U1.io.data_in    := io.data_in
  U1.io.fifo_empty := io.fifo_empty
  io.pop_wd_n      := U1.io.pop_wd_n
  io.data_out      := U1.io.data_out
  io.part_wd       := U1.io.part_wd
  io.pop_error     := U1.io.pop_error
}

object asymdata_outbuf extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate asymdata_outbuf") {
      def top = new asymdata_outbuf(16, 8, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
