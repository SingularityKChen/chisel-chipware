import chisel3._
import circt.stage._
import utest._

class addsub(val wA: Int = 4) extends RawModule {
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(wA.W))
    val B:   UInt = Input(UInt(wA.W))
    val CI:  Bool = Input(Bool())
    val SUB: Bool = Input(Bool())
    val Z:   UInt = Output(UInt(wA.W))
    val CO:  Bool = Output(Bool())
  })

  protected val U1: CW_addsub = Module(new CW_addsub(wA))
  U1.io.A   := io.A
  U1.io.B   := io.B
  U1.io.CI  := io.CI
  U1.io.SUB := io.SUB
  io.Z      := U1.io.Z
  io.CO     := U1.io.CO
}

object addsub extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate addsub") {
      def top = new addsub(4)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
