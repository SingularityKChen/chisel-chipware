import chisel3._
import chisel3.util.log2Ceil
import circt.stage._
import utest._

class b10b_enc(
  val bytes:       Int = 2,
  val k28_5_only:  Int = 0,
  val en_mode:     Int = 0,
  val init_mode:   Int = 0,
  val rst_mode:    Int = 0,
  val op_iso_mode: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val init_rd_n:   Bool  = Input(Bool())
    val init_rd_val: Bool  = Input(Bool())
    val k_char:      UInt  = Input(UInt(bytes.W))
    val data_in:     UInt  = Input(UInt((bytes * 8).W))
    val enable:      Bool  = Input(Bool())
    val rd:          Bool  = Output(Bool())
    val data_out:    UInt  = Output(UInt((bytes * 10).W))
  })

  protected val U1: CW_8b10b_enc = Module(
    new CW_8b10b_enc(bytes, k28_5_only, en_mode, init_mode, rst_mode, op_iso_mode)
  )
  U1.io.clk         := io.clk
  U1.io.rst_n       := io.rst_n
  U1.io.init_rd_n   := io.init_rd_n
  U1.io.init_rd_val := io.init_rd_val
  U1.io.k_char      := io.k_char
  U1.io.data_in     := io.data_in
  U1.io.enable      := io.enable
  io.rd             := U1.io.rd
  io.data_out       := U1.io.data_out
}

object b10b_enc extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate b10b_enc") {
      def top = new b10b_enc()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
