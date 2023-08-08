import chisel3._
import circt.stage._
import utest._

class addsub_dx(val width: Int = 4, val p1_width: Int = 2) extends RawModule {
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt(width.W))
    val b:      UInt = Input(UInt(width.W))
    val ci1:    Bool = Input(Bool())
    val ci2:    Bool = Input(Bool())
    val addsub: Bool = Input(Bool())
    val tc:     Bool = Input(Bool())
    val sat:    Bool = Input(Bool())
    val avg:    Bool = Input(Bool())
    val dplx:   Bool = Input(Bool())
    val sum:    UInt = Output(UInt(width.W))
    val co1:    Bool = Output(Bool())
    val co2:    Bool = Output(Bool())
  })

  protected val U1: CW_addsub_dx = Module(new CW_addsub_dx(width, p1_width))
  U1.io.a      := io.a
  U1.io.b      := io.b
  U1.io.ci1    := io.ci1
  U1.io.ci2    := io.ci2
  U1.io.addsub := io.addsub
  U1.io.tc     := io.tc
  U1.io.sat    := io.sat
  U1.io.avg    := io.avg
  U1.io.dplx   := io.dplx
  io.sum       := U1.io.sum
  io.co1       := U1.io.co1
  io.co2       := U1.io.co2
}

object addsub_dx extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate addsub_dx") {
      def top = new addsub_dx(4, 2)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
