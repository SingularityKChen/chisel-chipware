import chisel3._
import circt.stage._
import utest._

class prod_sum(val wAi: Int = 4, val wBi: Int = 5, val numinputs: Int = 4, val wZ: Int = 12) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt((wAi * numinputs).W))
    val B:  UInt = Input(UInt((wBi * numinputs).W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt(wZ.W))
  })

  protected val U1: CW_prod_sum = Module(new CW_prod_sum(wAi, wBi, numinputs, wZ))
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.TC := io.TC
  io.Z     := U1.io.Z
}

object prod_sum extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate prod_sum") {
      def top = new prod_sum(4, 5, 4, 12)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
