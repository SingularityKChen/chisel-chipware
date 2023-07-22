import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class sin(val A_width: Int = 34, val sin_width: Int = 34) extends RawModule {
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(A_width.W))
    val SIN: UInt = Output(UInt(sin_width.W))
  })

  protected val U1: CW_sin = Module(new CW_sin(A_width, sin_width))
  U1.io.A := io.A
  io.SIN  := U1.io.SIN
}

object sin extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate sin") {
      def top = new sin(34, 34)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
