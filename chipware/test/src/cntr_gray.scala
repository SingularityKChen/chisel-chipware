import chisel3._
import circt.stage._
import utest._

class cntr_gray(val width: Int = 4) extends RawModule {
  val io = IO(new Bundle {
    val clk:    Clock = Input(Clock())
    val rst_n:  Bool  = Input(Bool())
    val init_n: Bool  = Input(Bool())
    val load_n: Bool  = Input(Bool())
    val data:   UInt  = Input(UInt(width.W))
    val cen:    Bool  = Input(Bool())
    val count:  UInt  = Output(UInt(width.W))
  })

  protected val U1: CW_cntr_gray = Module(new CW_cntr_gray(width))
  U1.io.clk    := io.clk
  U1.io.rst_n  := io.rst_n
  U1.io.init_n := io.init_n
  U1.io.load_n := io.load_n
  U1.io.data   := io.data
  U1.io.cen    := io.cen
  io.count     := U1.io.count
}

object cntr_gray extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate cntr_gray") {
      def top = new cntr_gray(4)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
