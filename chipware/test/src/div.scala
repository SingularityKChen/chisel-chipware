import chisel3._
import circt.stage._
import utest._

class div(val a_width: Int = 8, val b_width: Int = 8, val tc_mode: Int = 0, val rem_mode: Int = 1, val arch: Int = 0)
    extends RawModule {
  protected val U1: CW_div = Module(new CW_div(a_width, b_width, tc_mode, rem_mode, arch))

  val io = IO(new Bundle {
    val a:           UInt = Input(UInt(a_width.W))
    val b:           UInt = Input(UInt(b_width.W))
    val quotient:    UInt = Output(UInt(a_width.W))
    val remainder:   UInt = Output(UInt(b_width.W))
    val divide_by_0: Bool = Output(Bool())
  })

  U1.io.a        := io.a
  U1.io.b        := io.b
  io.quotient    := U1.io.quotient
  io.remainder   := U1.io.remainder
  io.divide_by_0 := U1.io.divide_by_0
}

object div extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate div") {
      def top = new div(8, 8, 0, 1, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
