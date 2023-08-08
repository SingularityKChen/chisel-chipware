import chisel3._
import circt.stage._
import utest._

class lbsh(val A_width: Int = 8, val SH_width: Int = 3) extends RawModule {
  val io = IO(new Bundle {
    val A:     UInt = Input(UInt(A_width.W))
    val SH:    UInt = Input(UInt(SH_width.W))
    val SH_TC: Bool = Input(Bool())
    val B:     UInt = Output(UInt(A_width.W))
  })

  protected val U2: CW_lbsh = Module(new CW_lbsh(A_width, SH_width))
  U2.io.A     := io.A
  U2.io.SH    := io.SH
  U2.io.SH_TC := io.SH_TC
  io.B        := U2.io.B
}

object lbsh extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate lbsh") {
      def top = new lbsh(8, 3)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
