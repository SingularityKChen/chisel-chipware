import chisel3._
import circt.stage._
import utest._

class iir_sc(
  data_in_width:       Int = 4,
  data_out_width:      Int = 6,
  frac_data_out_width: Int = 0,
  feedback_width:      Int = 8,
  max_coef_width:      Int = 4,
  frac_coef_width:     Int = 0,
  saturation_mode:     Int = 1,
  out_reg:             Int = 1,
  A1_coef:             Int = -2,
  A2_coef:             Int = 3,
  B0_coef:             Int = 5,
  B1_coef:             Int = -6,
  B2_coef:             Int = -2)
    extends RawModule {

  val io = IO(new Bundle {
    val clk:        Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val init_n:     Bool  = Input(Bool())
    val enable:     Bool  = Input(Bool())
    val data_in:    UInt  = Input(UInt(data_in_width.W))
    val data_out:   UInt  = Output(UInt(data_out_width.W))
    val saturation: Bool  = Output(Bool())
  })

  protected val U1: CW_iir_sc = Module(
    new CW_iir_sc(
      data_in_width,
      data_out_width,
      frac_data_out_width,
      feedback_width,
      max_coef_width,
      frac_coef_width,
      saturation_mode,
      out_reg,
      A1_coef,
      A2_coef,
      B0_coef,
      B1_coef,
      B2_coef
    )
  )

  U1.io.clk     := io.clk
  U1.io.rst_n   := io.rst_n
  U1.io.init_n  := io.init_n
  U1.io.enable  := io.enable
  U1.io.data_in := io.data_in
  io.data_out   := U1.io.data_out
  io.saturation := U1.io.saturation
}

object iir_sc extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate CW_iir_scWrapper") {
      def top = new iir_sc(4, 6, 0, 8, 4, 0, 1, 1, -2, 3, 5, -6, -2)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
