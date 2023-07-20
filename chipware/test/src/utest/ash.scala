import chisel3._
import circt.stage._
import utest._

class ash(val wA: Int = 4, val wSH: Int = 2) extends RawModule {
  val io = IO(new Bundle {
    val A:       UInt = Input(UInt(wA.W))
    val DATA_TC: Bool = Input(Bool())
    val SH:      UInt = Input(UInt(wSH.W))
    val SH_TC:   Bool = Input(Bool())
    val Z:       UInt = Output(UInt(wA.W))
  })

  protected val U1: CW_ash = Module(new CW_ash(wA, wSH))
  U1.io.A       := io.A
  U1.io.DATA_TC := io.DATA_TC
  U1.io.SH      := io.SH
  U1.io.SH_TC   := io.SH_TC
  io.Z          := U1.io.Z
}

object ash extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ash") {
      def top = new ash()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
