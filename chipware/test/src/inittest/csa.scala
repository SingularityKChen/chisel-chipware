import chisel3._
import circt.stage._
import utest._

class csa(val width: Int = 4) extends RawModule {
  val io = IO(new Bundle {
    val a:     UInt = Input(UInt(width.W))
    val b:     UInt = Input(UInt(width.W))
    val c:     UInt = Input(UInt(width.W))
    val ci:    Bool = Input(Bool())
    val carry: UInt = Output(UInt(width.W))
    val sum:   UInt = Output(UInt(width.W))
    val co:    Bool = Output(Bool())
  })

  protected val U1: CW_csa = Module(new CW_csa(width))
  U1.io.a  := io.a
  U1.io.b  := io.b
  U1.io.c  := io.c
  U1.io.ci := io.ci
  io.carry := U1.io.carry
  io.sum   := U1.io.sum
  io.co    := U1.io.co
}

object csa extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate csa") {
      def top = new csa(4)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
