import chisel3._
import circt.stage._
import utest._

class b10b_dec(
  val bytes:       Int = 2,
  val k28_5_only:  Int = 0,
  val en_mode:     Int = 0,
  val init_mode:   Int = 0,
  val rst_mode:    Int = 0,
  val op_iso_mode: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val init_rd_n:    Bool  = Input(Bool())
    val init_rd_val:  Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt((bytes * 10).W))
    val error:        Bool  = Output(Bool())
    val rd:           Bool  = Output(Bool())
    val k_char:       UInt  = Output(UInt(bytes.W))
    val data_out:     UInt  = Output(UInt((bytes * 8).W))
    val rd_err:       Bool  = Output(Bool())
    val code_err:     Bool  = Output(Bool())
    val enable:       Bool  = Input(Bool())
    val rd_err_bus:   UInt  = Output(UInt(bytes.W))
    val code_err_bus: UInt  = Output(UInt(bytes.W))
  })
  protected val U1: CW_8b10b_dec = Module(
    new CW_8b10b_dec(
      bytes       = bytes,
      k28_5_only  = k28_5_only,
      en_mode     = en_mode,
      init_mode   = init_mode,
      rst_mode    = rst_mode,
      op_iso_mode = op_iso_mode
    )
  )
  U1.io.clk         := io.clk
  U1.io.rst_n       := io.rst_n
  U1.io.init_rd_n   := io.init_rd_n
  U1.io.init_rd_val := io.init_rd_val
  U1.io.data_in     := io.data_in
  io.error          := U1.io.error
  io.rd             := U1.io.rd
  io.k_char         := U1.io.k_char
  io.data_out       := U1.io.data_out
  io.rd_err         := U1.io.rd_err
  io.code_err       := U1.io.code_err
  U1.io.enable      := io.enable
  io.rd_err_bus     := U1.io.rd_err_bus
  io.code_err_bus   := U1.io.code_err_bus
}

object b10b_dec extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate b10b_dec") {
      def top = new b10b_dec()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
