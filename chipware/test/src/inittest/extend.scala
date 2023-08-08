import chisel3._
import circt.stage._
import utest._

class extend(val wA: Int = 8, val wZ: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt(wZ.W))
  })

  protected val U1: CW_extend = Module(new CW_extend(wA, wZ))
  U1.io.A  := io.A
  U1.io.TC := io.TC
  io.Z     := U1.io.Z
}

object extend extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate extend") {
      def top = new extend(8, 8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
