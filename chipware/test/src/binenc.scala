import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class binenc(val wA: Int = 2, val wZ: Int = 1) extends RawModule {
  protected val bit_width_wA: Int = log2Ceil(wA)
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(wA.W))
    val Z:   UInt = Output(UInt(wZ.W))
    val ERR: Bool = Output(Bool())
  })

  protected val U1: CW_binenc = Module(new CW_binenc(wA, wZ))
  U1.io.A := io.A
  io.Z    := U1.io.Z
  io.ERR  := U1.io.ERR
}

object binenc extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate binenc") {
      def top = new binenc(2, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
