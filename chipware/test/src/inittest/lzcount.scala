import chisel3._
import circt.stage._
import utest._

class lzcount(val wA: Int = 2, val wZ: Int = 1) extends RawModule {
  val io = IO(new Bundle {
    val A:    UInt = Input(UInt(wA.W))
    val Z:    UInt = Output(UInt(wZ.W))
    val All0: Bool = Output(Bool())
  })

  protected val U1: CW_lzcount = Module(new CW_lzcount(wA, wZ))
  U1.io.A := io.A
  io.Z    := U1.io.Z
  io.All0 := U1.io.All0
}

object lzcount extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate lzcount") {
      def top = new lzcount(2, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
