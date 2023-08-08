import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class minmax(val width: Int = 4, val num_inputs: Int = 2) extends RawModule {
  protected val log2_num_inputs: Int = log2Ceil(num_inputs)
  val io = IO(new Bundle {
    val a:       UInt = Input(UInt((num_inputs * width).W))
    val tc:      Bool = Input(Bool())
    val min_max: Bool = Input(Bool())
    val value:   UInt = Output(UInt(width.W))
    val index:   UInt = Output(UInt(log2_num_inputs.W))
  })

  protected val U1: CW_minmax = Module(new CW_minmax(width, num_inputs))
  U1.io.a       := io.a
  U1.io.tc      := io.tc
  U1.io.min_max := io.min_max
  io.value      := U1.io.value
  io.index      := U1.io.index
}

object minmax extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate minmax") {
      def top = new minmax(4, 2)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
