import chisel3._
import circt.stage._
import utest._

class cos(val A_width: Int = 34, val cos_width: Int = 34) extends RawModule {
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(A_width.W))
    val COS: UInt = Output(UInt(cos_width.W))
  })

  protected val U1: CW_cos = Module(new CW_cos(A_width, cos_width))
  U1.io.A := io.A
  io.COS  := U1.io.COS
}

object cos extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate cos") {
      def top = new cos(34, 34)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
