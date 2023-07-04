import chisel3._
import circt.stage._
import utest._

class bsh(val A_width: Int = 8, val SH_width: Int = 3) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(A_width.W))
    val SH: UInt = Input(UInt(SH_width.W))
    val B:  UInt = Output(UInt(A_width.W))
  })

  protected val U1: CW_bsh = Module(new CW_bsh(A_width, SH_width))
  U1.io.A  := io.A
  U1.io.SH := io.SH
  io.B     := U1.io.B
}

object bsh extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate bsh") {
      def top = new bsh()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
