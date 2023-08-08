import chisel3._
import circt.stage._
import utest._

class square(val wA: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt((2 * wA - 1).W))
  })

  protected val U1: CW_square = Module(new CW_square(wA))
  U1.io.A  := io.A
  U1.io.TC := io.TC
  io.Z     := U1.io.Z
}

object square extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate square") {
      def top = new square(32)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
