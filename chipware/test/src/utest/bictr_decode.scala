import chisel3._
import circt.stage._
import utest._

class bictr_decode(val WIDTH: Int = 3) extends RawModule {
  val io = IO(new Bundle {
    val data:      UInt  = Input(UInt(WIDTH.W))
    val up_dn:     Bool  = Input(Bool())
    val cen:       Bool  = Input(Bool())
    val load:      Bool  = Input(Bool())
    val clk:       Clock = Input(Clock())
    val reset:     Bool  = Input(Bool())
    val count_dec: UInt  = Output(UInt((1 << WIDTH).W))
    val tercnt:    Bool  = Output(Bool())
  })

  protected val U1: CW_bictr_decode = Module(new CW_bictr_decode(WIDTH))
  U1.io.data   := io.data
  U1.io.up_dn  := io.up_dn
  U1.io.cen    := io.cen
  U1.io.load   := io.load
  U1.io.clk    := io.clk
  U1.io.reset  := io.reset
  io.count_dec := U1.io.count_dec
  io.tercnt    := U1.io.tercnt
}

object bictr_decode extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate bictr_decode") {
      def top = new bictr_decode(3)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
