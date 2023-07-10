import chisel3._
import circt.stage._
import utest._

class asymdata_inbuf(
  val in_width:    Int = 8,
  val out_width:   Int = 16,
  val err_mode:    Int = 0,
  val byte_order:  Int = 0,
  val flush_value: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk_push:    Clock = Input(Clock())
    val rst_push_n:  Bool  = Input(Bool())
    val init_push_n: Bool  = Input(Bool())
    val push_req_n:  Bool  = Input(Bool())
    val data_in:     UInt  = Input(UInt(in_width.W))
    val flush_n:     Bool  = Input(Bool())
    val fifo_full:   Bool  = Input(Bool())
    val data_out:    UInt  = Output(UInt(out_width.W))
    val inbuf_full:  Bool  = Output(Bool())
    val push_wd_n:   Bool  = Output(Bool())
    val part_wd:     Bool  = Output(Bool())
    val push_error:  Bool  = Output(Bool())
  })

  protected val U1: CW_asymdata_inbuf = Module(
    new CW_asymdata_inbuf(in_width, out_width, err_mode, byte_order, flush_value)
  )
  U1.io.clk_push    := io.clk_push
  U1.io.rst_push_n  := io.rst_push_n
  U1.io.init_push_n := io.init_push_n
  U1.io.push_req_n  := io.push_req_n
  U1.io.data_in     := io.data_in
  U1.io.flush_n     := io.flush_n
  U1.io.fifo_full   := io.fifo_full
  io.data_out       := U1.io.data_out
  io.inbuf_full     := U1.io.inbuf_full
  io.push_wd_n      := U1.io.push_wd_n
  io.part_wd        := U1.io.part_wd
  io.push_error     := U1.io.push_error
}

object asymdata_inbuf extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate asymdata_inbuf") {
      def top = new asymdata_inbuf(8, 16, 0, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
