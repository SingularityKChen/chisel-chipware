import chisel3._
import circt.stage._
import utest._

class mux_any(val wA: Int = 8, val wS: Int = 2, val wZ: Int = 2) extends RawModule {
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val S: UInt = Input(UInt(wS.W))
    val Z: UInt = Output(UInt(wZ.W))
  })
  protected val U1: CW_mux_any = Module(new CW_mux_any(wA, wS, wZ))
  U1.io.A := io.A
  U1.io.S := io.S
  io.Z    := U1.io.Z
}

object mux_any extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate mux_any") {
      def top = new mux_any()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
