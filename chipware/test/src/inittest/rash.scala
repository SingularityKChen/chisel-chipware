import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class rash(val wA: Int = 4, val wSH: Int = 2) extends RawModule {
  protected val bit_width_wA: Int = log2Ceil(wA)
  val io = IO(new Bundle {
    val A:       UInt = Input(UInt(wA.W))
    val DATA_TC: Bool = Input(Bool())
    val SH:      UInt = Input(UInt(wSH.W))
    val SH_TC:   Bool = Input(Bool())
    val Z:       UInt = Output(UInt(wA.W))
  })

  protected val U1: CW_rash = Module(new CW_rash(wA, wSH))
  U1.io.A       := io.A
  U1.io.DATA_TC := io.DATA_TC
  U1.io.SH      := io.SH
  U1.io.SH_TC   := io.SH_TC
  io.Z          := U1.io.Z
}

object rash extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate rash") {
      def top = new rash(4, 2)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
