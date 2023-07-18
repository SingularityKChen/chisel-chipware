import chisel3._
import circt.stage._
import utest._

class shftreg(val length: Int = 4) extends RawModule {
  val io = IO(new Bundle {
    val clk:     Clock = Input(Clock())
    val s_in:    Bool  = Input(Bool())
    val p_in:    UInt  = Input(UInt(length.W))
    val shift_n: Bool  = Input(Bool())
    val load_n:  Bool  = Input(Bool())
    val p_out:   UInt  = Output(UInt(length.W))
  })
  protected val U1: CW_shftreg = Module(new CW_shftreg(length))
  U1.io.clk     := io.clk
  U1.io.s_in    := io.s_in
  U1.io.p_in    := io.p_in
  U1.io.shift_n := io.shift_n
  U1.io.load_n  := io.load_n
  io.p_out      := U1.io.p_out
}

object shftreg extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate shftreg") {
      def top = new shftreg(4)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
