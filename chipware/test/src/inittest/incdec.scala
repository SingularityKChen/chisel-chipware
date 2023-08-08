import chisel3._
import circt.stage._
import utest._

class incdec(val wA: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(wA.W))
    val DEC: Bool = Input(Bool())
    val Z:   UInt = Output(UInt(wA.W))
  })

  protected val U1: CW_incdec = Module(new CW_incdec(wA))
  U1.io.A   := io.A
  U1.io.DEC := io.DEC
  io.Z      := U1.io.Z
}

object incdec extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate incdec") {
      def top = new incdec(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
