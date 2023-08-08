import chisel3._
import circt.stage._
import utest._

class norm(val a_width: Int = 8, val srch_wind: Int = 8, val exp_width: Int = 4, val exp_ctr: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val a:          UInt = Input(UInt(a_width.W))
    val exp_offset: UInt = Input(UInt(exp_width.W))
    val no_detect:  Bool = Output(Bool())
    val ovfl:       Bool = Output(Bool())
    val b:          UInt = Output(UInt(a_width.W))
    val exp_adj:    UInt = Output(UInt(exp_width.W))
  })

  protected val U1: CW_norm = Module(new CW_norm(a_width, srch_wind, exp_width, exp_ctr))
  U1.io.a          := io.a
  U1.io.exp_offset := io.exp_offset
  io.no_detect     := U1.io.no_detect
  io.ovfl          := U1.io.ovfl
  io.b             := U1.io.b
  io.exp_adj       := U1.io.exp_adj
}

object norm extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate norm") {
      def top = new norm(8, 8, 4, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
