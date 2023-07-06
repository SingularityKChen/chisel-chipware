import chisel3._
import circt.stage._
import utest._

class cmp_dx(val width: Int = 8, val p1_width: Int = 4) extends RawModule {
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(width.W))
    val b:    UInt = Input(UInt(width.W))
    val tc:   Bool = Input(Bool())
    val dplx: Bool = Input(Bool())
    val lt1:  Bool = Output(Bool())
    val eq1:  Bool = Output(Bool())
    val gt1:  Bool = Output(Bool())
    val lt2:  Bool = Output(Bool())
    val eq2:  Bool = Output(Bool())
    val gt2:  Bool = Output(Bool())
  })
  protected val U1: CW_cmp_dx = Module(new CW_cmp_dx(width, p1_width))
  U1.io.a    := io.a
  U1.io.b    := io.b
  U1.io.tc   := io.tc
  U1.io.dplx := io.dplx
  io.lt1     := U1.io.lt1
  io.eq1     := U1.io.eq1
  io.gt1     := U1.io.gt1
  io.lt2     := U1.io.lt2
  io.eq2     := U1.io.eq2
  io.gt2     := U1.io.gt2
}

object cmp_dx extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate cmp_dx") {
      def top = new cmp_dx(8, 4)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
