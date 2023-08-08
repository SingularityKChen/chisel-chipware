import chisel3._
import circt.stage._
import utest._

class lfsr_updn(val width: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val updn:   Bool  = Input(Bool())
    val cen:    Bool  = Input(Bool())
    val clk:    Clock = Input(Clock())
    val reset:  Bool  = Input(Bool())
    val count:  UInt  = Output(UInt(width.W))
    val tercnt: Bool  = Output(Bool())
  })

  protected val U1: CW_lfsr_updn = Module(new CW_lfsr_updn(width))
  U1.io.updn  := io.updn
  U1.io.cen   := io.cen
  U1.io.clk   := io.clk
  U1.io.reset := io.reset
  io.count    := U1.io.count
  io.tercnt   := U1.io.tercnt
}

object lfsr_updn extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate lfsr_updn") {
      def top = new lfsr_updn(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
