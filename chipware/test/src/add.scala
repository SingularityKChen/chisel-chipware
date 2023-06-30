import chisel3._
import circt.stage._
import utest._

class add(val wA: Int = 4) extends RawModule {
  require(wA >= 1, "wA must be >= 1")
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wA.W))
    val CI: UInt = Input(UInt(1.W))
    val Z:  UInt = Output(UInt(wA.W))
    val CO: UInt = Output(UInt(1.W))
  })
  protected val U1: CW_add = Module(new CW_add(wA))
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.CI := io.CI
  io.Z     := U1.io.Z
  io.CO    := U1.io.CO
}

object add extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate add") {
      def top = new add()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
