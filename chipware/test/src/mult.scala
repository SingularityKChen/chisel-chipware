// filename: mult.scala
import chisel3._
import circt.stage._
import utest._

class mult(val wA: Int = 8, val wB: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wB.W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt((wA + wB - 1).W))
  })

  protected val U1: CW_mult = Module(new CW_mult(wA, wB))
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.TC := io.TC
  io.Z     := U1.io.Z
}

object mult extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate mult") {
      def top = new mult()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
