import chisel3._
import circt.stage._
import utest._

class lfsr_scnto(val width: Int = 8, val count_to: Int = 5) extends RawModule {
  val io = IO(new Bundle {
    val data:   UInt  = Input(UInt(width.W))
    val load:   Bool  = Input(Bool())
    val cen:    Bool  = Input(Bool())
    val clk:    Clock = Input(Clock())
    val reset:  Bool  = Input(Bool())
    val count:  UInt  = Output(UInt(width.W))
    val tercnt: Bool  = Output(Bool())
  })

  protected val U1: CW_lfsr_scnto = Module(new CW_lfsr_scnto(width, count_to))
  U1.io.data  := io.data
  U1.io.load  := io.load
  U1.io.cen   := io.cen
  U1.io.clk   := io.clk
  U1.io.reset := io.reset
  io.count    := U1.io.count
  io.tercnt   := U1.io.tercnt
}

object lfsr_scnto extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate lfsr_scnto") {
      def top = new lfsr_scnto(8, 5)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
