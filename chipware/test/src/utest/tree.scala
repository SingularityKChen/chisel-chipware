import chisel3._
import circt.stage._
import utest._

class tree(val num_inputs: Int = 8, val input_width: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val INPUT: UInt = Input(UInt((num_inputs * input_width).W))
    val OUT0:  UInt = Output(UInt(input_width.W))
    val OUT1:  UInt = Output(UInt(input_width.W))
  })

  protected val U1: CW_tree = Module(new CW_tree(num_inputs, input_width))
  U1.io.INPUT := io.INPUT
  io.OUT0     := U1.io.OUT0
  io.OUT1     := U1.io.OUT1
}

object tree extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate tree") {
      def top = new tree(8, 8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
