import chisel3._
import circt.stage._
import utest._

class b10b_unbal(val k28_5_only: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val k_char:  Bool = Input(Bool())
    val data_in: UInt = Input(UInt(8.W))
    val unbal:   Bool = Output(Bool())
  })

  require(k28_5_only >= 0 && k28_5_only <= 1, s"k28_5_only must be in the range [0, 1]. Got $k28_5_only.")

  protected val U1: CW_8b10b_unbal = Module(new CW_8b10b_unbal(k28_5_only))
  U1.io.k_char  := io.k_char
  U1.io.data_in := io.data_in
  io.unbal      := U1.io.unbal
}

object b10b_unbal extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate b10b_unbal") {
      def top = new b10b_unbal()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
