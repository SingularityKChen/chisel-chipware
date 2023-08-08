import chisel3._
import circt.stage._
import utest._

class sub(val wA: Int = 8) extends RawModule {
  protected val bit_width_wA: Int = wA
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wA.W))
    val CI: Bool = Input(Bool())
    val Z:  UInt = Output(UInt(wA.W))
    val CO: Bool = Output(Bool())
  })

  protected val U1: CW_sub = Module(new CW_sub(wA))
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.CI := io.CI
  io.Z     := U1.io.Z
  io.CO    := U1.io.CO
}

object sub extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate sub") {
      def top = new sub(32)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
