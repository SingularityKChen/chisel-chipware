import chisel3._
import circt.stage._
import utest._

class compge(val wA: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(wA.W))
    val B:   UInt = Input(UInt(wA.W))
    val TC:  Bool = Input(Bool())
    val AGB: Bool = Output(Bool())
    val AEB: Bool = Output(Bool())
  })

  protected val U1: CW_compge = Module(new CW_compge(wA))
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.TC := io.TC
  io.AGB   := U1.io.AGB
  io.AEB   := U1.io.AEB
}

object compge extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate compge") {
      def top = new compge(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
