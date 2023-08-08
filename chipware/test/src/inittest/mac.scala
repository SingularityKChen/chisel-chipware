import chisel3._
import circt.stage._
import utest._

class mac(val A_width: Int = 8, val B_width: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val TC:  Bool = Input(Bool())
    val A:   UInt = Input(UInt(A_width.W))
    val B:   UInt = Input(UInt(B_width.W))
    val C:   UInt = Input(UInt((A_width + B_width).W))
    val MAC: UInt = Output(UInt((A_width + B_width).W))
  })

  protected val U1: CW_mac = Module(new CW_mac(A_width, B_width))
  U1.io.TC := io.TC
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.C  := io.C
  io.MAC   := U1.io.MAC
}

object mac extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate mac") {
      def top = new mac(8, 8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
