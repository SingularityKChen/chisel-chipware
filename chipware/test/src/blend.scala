import chisel3._
import circt.stage._
import utest._

class blend(val wX: Int = 8, val wA: Int = 8) extends RawModule {
  protected val U1: CW_blend = Module(new CW_blend(wX, wA))

  val io = IO(new Bundle {
    val X0:     UInt = Input(UInt(wX.W))
    val X1:     UInt = Input(UInt(wX.W))
    val TC:     Bool = Input(Bool())
    val Alpha:  UInt = Input(UInt(wA.W))
    val Alpha1: Bool = Input(Bool())
    val Z:      UInt = Output(UInt((wX + wA).W))
  })

  U1.io.X0     := io.X0
  U1.io.X1     := io.X1
  U1.io.TC     := io.TC
  U1.io.Alpha  := io.Alpha
  U1.io.Alpha1 := io.Alpha1
  io.Z         := U1.io.Z
}

object blend extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate blend") {
      def top = new blend()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
