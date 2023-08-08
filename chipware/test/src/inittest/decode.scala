import chisel3._
import circt.stage._
import utest._

class decode(val wA: Int = 4) extends RawModule {
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val Z: UInt = Output(UInt(((1 << wA) - 1).W))
  })

  protected val U1: CW_decode = Module(new CW_decode(wA))
  U1.io.A := io.A
  io.Z    := U1.io.Z
}

object decode extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate decode") {
      def top = new decode(4)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
