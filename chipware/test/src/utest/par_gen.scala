import chisel3._
import circt.stage._
import utest._

class par_gen(val width: Int = 8, val par_type: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val datain: UInt = Input(UInt(width.W))
    val parity: UInt = Output(UInt(1.W))
  })

  protected val U1: CW_par_gen = Module(new CW_par_gen(width, par_type))
  U1.io.datain := io.datain
  io.parity    := U1.io.parity
}

object par_gen extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate par_gen") {
      def top = new par_gen(8, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
