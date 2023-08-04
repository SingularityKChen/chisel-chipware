import chisel3._
import circt.stage._
import utest._

class logicop(val wS: Int = 8) extends RawModule {
  require(wS >= 1, "wS must be >= 1")

  val io = IO(new Bundle {
    val S:  UInt = Input(UInt(wS.W))
    val D:  UInt = Input(UInt(wS.W))
    val OP: UInt = Input(UInt(4.W))
    val Z:  UInt = Output(UInt(wS.W))
  })

  protected val U1: CW_logicop = Module(new CW_logicop(wS))
  U1.io.S  := io.S
  U1.io.D  := io.D
  U1.io.OP := io.OP
  io.Z     := U1.io.Z
}

object logicop extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate LogicOp") {
      def top = new logicop(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
