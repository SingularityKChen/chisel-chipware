import chisel3._
import circt.stage._
import utest._

class pricod(val a_width: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(a_width.W))
    val cod:  UInt = Output(UInt(a_width.W))
    val zero: Bool = Output(Bool())
  })

  protected val U1: CW_pricod = Module(new CW_pricod(a_width))
  U1.io.a := io.a
  io.cod  := U1.io.cod
  io.zero := U1.io.zero
}

object pricod extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate pricod") {
      def top = new pricod(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
