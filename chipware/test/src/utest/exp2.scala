import chisel3._
import circt.stage._
import utest._

class exp2(val op_width: Int = 8, val arch: Int = 0, val err_range: Int = 1) extends RawModule {
  val io = IO(new Bundle {
    val a: UInt = Input(UInt(op_width.W))
    val z: UInt = Output(UInt(op_width.W))
  })

  protected val U1: CW_exp2 = Module(new CW_exp2(op_width, arch, err_range))
  U1.io.a := io.a
  io.z    := U1.io.z
}

object exp2 extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate exp2") {
      def top = new exp2(8, 0, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
