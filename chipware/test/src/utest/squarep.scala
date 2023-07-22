import chisel3._
import circt.stage._
import utest._

class squarep(val width: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(width.W))
    val tc:   Bool = Input(Bool())
    val out0: UInt = Output(UInt((width * 2).W))
    val out1: UInt = Output(UInt((width * 2).W))
  })

  protected val U1: CW_squarep = Module(new CW_squarep(width))
  U1.io.a  := io.a
  U1.io.tc := io.tc
  io.out0  := U1.io.out0
  io.out1  := U1.io.out1
}

object squarep extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate squarep") {
      def top = new squarep(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
