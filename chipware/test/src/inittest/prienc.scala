import chisel3._
import circt.stage._
import utest._

class prienc(val wA: Int = 2, val wZ: Int = 2) extends RawModule {
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val Z: UInt = Output(UInt(wZ.W))
  })

  protected val U1: CW_prienc = Module(new CW_prienc(wA, wZ))
  U1.io.A := io.A
  io.Z    := U1.io.Z
}

object prienc extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate prienc") {
      def top = new prienc(2, 2)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
