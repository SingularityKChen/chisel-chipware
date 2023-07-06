import chisel3._
import circt.stage._
import utest._

class cmp6(val wA: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wA.W))
    val TC: Bool = Input(Bool())
    val LT: Bool = Output(Bool())
    val GT: Bool = Output(Bool())
    val EQ: Bool = Output(Bool())
    val LE: Bool = Output(Bool())
    val GE: Bool = Output(Bool())
    val NE: Bool = Output(Bool())
  })

  protected val U1: CW_cmp6 = Module(new CW_cmp6(wA))
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.TC := io.TC
  io.LT    := U1.io.LT
  io.GT    := U1.io.GT
  io.EQ    := U1.io.EQ
  io.LE    := U1.io.LE
  io.GE    := U1.io.GE
  io.NE    := U1.io.NE
}

object cmp6 extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate cmp6") {
      def top = new cmp6(8)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
