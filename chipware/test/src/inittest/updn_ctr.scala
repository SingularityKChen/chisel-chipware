import chisel3._
import circt.stage._
import utest._

class updn_ctr(val WIDTH: Int = 3) extends RawModule {
  val io = IO(new Bundle {
    val data:   UInt  = Input(UInt(WIDTH.W))
    val up_dn:  Bool  = Input(Bool())
    val load:   Bool  = Input(Bool())
    val cen:    Bool  = Input(Bool())
    val clk:    Clock = Input(Clock())
    val reset:  Bool  = Input(Bool())
    val count:  UInt  = Output(UInt(WIDTH.W))
    val tercnt: Bool  = Output(Bool())
  })

  protected val U1: CW_updn_ctr = Module(new CW_updn_ctr(WIDTH))
  U1.io.data  := io.data
  U1.io.up_dn := io.up_dn
  U1.io.load  := io.load
  U1.io.cen   := io.cen
  U1.io.clk   := io.clk
  U1.io.reset := io.reset
  io.count    := U1.io.count
  io.tercnt   := U1.io.tercnt
}

object updn_ctr extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate updn_ctr") {
      def top = new updn_ctr(3)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
