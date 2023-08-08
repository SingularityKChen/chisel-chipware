import chisel3._
import circt.stage._
import utest._

class multp(val a_width: Int = 8, val b_width: Int = 8, val out_width: Int = 18) extends RawModule {
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(a_width.W))
    val b:    UInt = Input(UInt(b_width.W))
    val tc:   Bool = Input(Bool())
    val out0: UInt = Output(UInt(out_width.W))
    val out1: UInt = Output(UInt(out_width.W))
  })

  protected val U1: CW_multp = Module(new CW_multp(a_width, b_width, out_width))
  U1.io.a  := io.a
  U1.io.b  := io.b
  U1.io.tc := io.tc
  io.out0  := U1.io.out0
  io.out1  := U1.io.out1
}

object multp extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate multp") {
      def top = new multp()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
