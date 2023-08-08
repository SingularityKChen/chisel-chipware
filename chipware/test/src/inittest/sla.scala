import chisel3._
import circt.stage._
import utest._

class sla(val A_width: Int = 4, val SH_width: Int = 2) extends RawModule {
  val io = IO(new Bundle {
    val A:     UInt = Input(UInt(A_width.W))
    val SH:    UInt = Input(UInt(SH_width.W))
    val SH_TC: Bool = Input(Bool())
    val B:     UInt = Output(UInt(A_width.W))
  })

  protected val U1: CW_sla = Module(new CW_sla(A_width, SH_width))
  U1.io.A     := io.A
  U1.io.SH    := io.SH
  U1.io.SH_TC := io.SH_TC
  io.B        := U1.io.B
}

object sla extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate sla") {
      def top = new sla(4, 2)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
