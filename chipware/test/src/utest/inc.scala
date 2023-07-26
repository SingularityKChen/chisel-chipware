import chisel3._
import circt.stage._
import utest._

class inc(val wA: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val Z: UInt = Output(UInt(wA.W))
  })

  protected val U1: CW_inc = Module(new CW_inc(wA))
  U1.io.A := io.A
  io.Z    := U1.io.Z
}

object inc extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate inc") {
      def top = new inc(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
