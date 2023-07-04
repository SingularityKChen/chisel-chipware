import chisel3._
import circt.stage._
import utest._

class bictr_dcnto(val WIDTH: Int = 3) extends RawModule {
  val io = IO(new Bundle {
    val data:     UInt  = Input(UInt(WIDTH.W))
    val count_to: UInt  = Input(UInt(WIDTH.W))
    val up_dn:    Bool  = Input(Bool())
    val load:     Bool  = Input(Bool())
    val cen:      Bool  = Input(Bool())
    val clk:      Clock = Input(Clock())
    val reset:    Bool  = Input(Bool())
    val count:    UInt  = Output(UInt(WIDTH.W))
    val tercnt:   Bool  = Output(Bool())
  })

  protected val U0: CW_bictr_dcnto = Module(new CW_bictr_dcnto(WIDTH))
  U0.io.data     := io.data
  U0.io.count_to := io.count_to
  U0.io.up_dn    := io.up_dn
  U0.io.load     := io.load
  U0.io.cen      := io.cen
  U0.io.clk      := io.clk
  U0.io.reset    := io.reset
  io.count       := U0.io.count
  io.tercnt      := U0.io.tercnt
}

object bictr_dcnto extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate bictr_dcnto") {
      def top = new bictr_dcnto()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
