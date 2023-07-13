import chisel3._
import circt.stage._
import utest._

class ffrst(val wD: Int = 1) extends RawModule {
  protected val U1: CW_ffrst = Module(new CW_ffrst(wD))
  val io = IO(new Bundle {
    val CLK:  Clock = Input(Clock())
    val RST:  Bool  = Input(Bool())
    val D:    UInt  = Input(UInt(wD.W))
    val RSTD: UInt  = Input(UInt(wD.W))
    val Q:    UInt  = Output(UInt(wD.W))
  })
  U1.io.CLK  := io.CLK
  U1.io.RST  := io.RST
  U1.io.D    := io.D
  U1.io.RSTD := io.RSTD
  io.Q       := U1.io.Q
}

object ffrst extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ffrst") {
      def top = new ffrst(1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
