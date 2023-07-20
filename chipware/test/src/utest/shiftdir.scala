import chisel3._
import circt.stage._
import utest._

class shiftdir(val wA: Int = 2, val wSH: Int = 1) extends RawModule {
  val io = IO(new Bundle {
    val A:    UInt = Input(UInt(wA.W))
    val SH:   UInt = Input(UInt(wSH.W))
    val LEFT: Bool = Input(Bool())
    val TC:   Bool = Input(Bool())
    val Z:    UInt = Output(UInt(wA.W))
  })
  protected val U1: CW_shiftdir = Module(new CW_shiftdir(wA, wSH))
  U1.io.A    := io.A
  U1.io.SH   := io.SH
  U1.io.LEFT := io.LEFT
  U1.io.TC   := io.TC
  io.Z       := U1.io.Z
}

object shiftdir extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate shiftdir") {
      def top = new shiftdir(2, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
