import chisel3._
import circt.stage._
import utest._

class shad_reg(val width: Int = 8, val bld_shad_reg: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val datain:   UInt  = Input(UInt(width.W))
    val sys_clk:  Clock = Input(Clock())
    val shad_clk: Clock = Input(Clock())
    val reset:    Bool  = Input(Bool())
    val SI:       Bool  = Input(Bool())
    val SE:       Bool  = Input(Bool())
    val sys_out:  UInt  = Output(UInt(width.W))
    val shad_out: UInt  = Output(UInt(width.W))
    val SO:       Bool  = Output(Bool())
  })
  protected val U1: CW_shad_reg = Module(new CW_shad_reg(width, bld_shad_reg))
  U1.io.datain   := io.datain
  U1.io.sys_clk  := io.sys_clk
  U1.io.shad_clk := io.shad_clk
  U1.io.reset    := io.reset
  U1.io.SI       := io.SI
  U1.io.SE       := io.SE
  io.sys_out     := U1.io.sys_out
  io.shad_out    := U1.io.shad_out
  io.SO          := U1.io.SO
}

object shad_reg extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate shad_reg") {
      def top = new shad_reg(8, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
