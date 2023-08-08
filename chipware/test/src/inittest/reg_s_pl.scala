import chisel3._
import circt.stage._
import utest._

class reg_s_pl(val width: Int, val reset_value: Int) extends RawModule {
  val io = IO(new Bundle {
    val d:       UInt  = Input(UInt(width.W))
    val clk:     Clock = Input(Clock())
    val reset_N: Bool  = Input(Bool())
    val enable:  Bool  = Input(Bool())
    val q:       UInt  = Output(UInt(width.W))
  })

  // Instantiate the BlackBox
  protected val U1: CW_reg_s_pl = Module(new CW_reg_s_pl(width, reset_value))
  U1.io.d       := io.d
  U1.io.clk     := io.clk
  U1.io.reset_N := io.reset_N
  U1.io.enable  := io.enable
  io.q          := U1.io.q
}

// Define the TestSuite
object reg_s_pl extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate reg_s_pl") {
      def top = new reg_s_pl(8, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
