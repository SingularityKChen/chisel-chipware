import chisel3._
import circt.stage._
import utest._

class iir_dc(
  val data_in_width:       Int = 8,
  val data_out_width:      Int = 16,
  val frac_data_out_width: Int = 4,
  val feedback_width:      Int = 12,
  val max_coef_width:      Int = 8,
  val frac_coef_width:     Int = 4,
  val saturation_mode:     Int = 0,
  val out_reg:             Int = 1)
    extends RawModule {

  val io = IO(new Bundle {
    val clk:        Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val init_n:     Bool  = Input(Bool())
    val enable:     Bool  = Input(Bool())
    val A1_coef:    SInt  = Input(SInt(max_coef_width.W))
    val A2_coef:    SInt  = Input(SInt(max_coef_width.W))
    val B0_coef:    SInt  = Input(SInt(max_coef_width.W))
    val B1_coef:    SInt  = Input(SInt(max_coef_width.W))
    val B2_coef:    SInt  = Input(SInt(max_coef_width.W))
    val data_in:    SInt  = Input(SInt(data_in_width.W))
    val data_out:   SInt  = Output(SInt(data_out_width.W))
    val saturation: Bool  = Output(Bool())
  })

  protected val U1: CW_iir_dc = Module(
    new CW_iir_dc(
      data_in_width,
      data_out_width,
      frac_data_out_width,
      feedback_width,
      max_coef_width,
      frac_coef_width,
      saturation_mode,
      out_reg
    )
  )
  U1.io.clk     := io.clk
  U1.io.rst_n   := io.rst_n
  U1.io.init_n  := io.init_n
  U1.io.enable  := io.enable
  U1.io.A1_coef := io.A1_coef
  U1.io.A2_coef := io.A2_coef
  U1.io.B0_coef := io.B0_coef
  U1.io.B1_coef := io.B1_coef
  U1.io.B2_coef := io.B2_coef
  U1.io.data_in := io.data_in
  io.data_out   := U1.io.data_out
  io.saturation := U1.io.saturation
}

object iir_dc extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate iir_dc") {
      def top = new iir_dc(8, 16, 4, 12, 8, 4, 0, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
