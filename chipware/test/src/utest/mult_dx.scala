import chisel3._
import circt.stage._
import utest._

class mult_dx(val width: Int = 16, val p1_width: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val a:       UInt = Input(UInt(width.W))
    val b:       UInt = Input(UInt(width.W))
    val tc:      Bool = Input(Bool())
    val dplx:    Bool = Input(Bool())
    val product: UInt = Output(UInt((width * 2).W))
  })
  protected val U1: CW_mult_dx = Module(new CW_mult_dx(width, p1_width))
  U1.io.a    := io.a
  U1.io.b    := io.b
  U1.io.tc   := io.tc
  U1.io.dplx := io.dplx
  io.product := U1.io.product
}

object mult_dx extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate mult_dx") {
      def top = new mult_dx()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
