import chisel3._
import circt.stage._
import utest._

class ffrsten(val wD: Int = 1) extends RawModule {
  require(wD >= 1, s"Data width must be >= 1, but got $wD")
  val io = IO(new Bundle {
    val CLK:  Clock = Input(Clock())
    val RST:  Bool  = Input(Bool())
    val EN:   Bool  = Input(Bool())
    val D:    UInt  = Input(UInt(wD.W))
    val RSTD: UInt  = Input(UInt(wD.W))
    val Q:    UInt  = Output(UInt(wD.W))
  })

  protected val U1: CW_ffrsten = Module(new CW_ffrsten(wD))
  U1.io.CLK  := io.CLK
  U1.io.RST  := io.RST
  U1.io.EN   := io.EN
  U1.io.D    := io.D
  U1.io.RSTD := io.RSTD
  io.Q       := U1.io.Q
}

object ffrsten extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ffrsten") {
      def top = new ffrsten(1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
