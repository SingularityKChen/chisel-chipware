import chisel3._
import chisel3.util.log2Ceil
import circt.stage._
import utest._

class ram_rw_a_dff(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val rst_n:     Bool  = Input(Bool())
    val cs_n:      Bool  = Input(Bool())
    val wr_n:      Bool  = Input(Bool())
    val test_mode: Bool  = Input(Bool())
    val test_clk:  Clock = Input(Clock())
    val rw_addr:   UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:   UInt  = Input(UInt(data_width.W))
    val data_out:  UInt  = Output(UInt(data_width.W))
  })

  protected val U1: CW_ram_rw_a_dff = Module(new CW_ram_rw_a_dff(data_width, depth, rst_mode))
  U1.io.rst_n     := io.rst_n
  U1.io.cs_n      := io.cs_n
  U1.io.wr_n      := io.wr_n
  U1.io.test_mode := io.test_mode
  U1.io.test_clk  := io.test_clk
  U1.io.rw_addr   := io.rw_addr
  U1.io.data_in   := io.data_in
  io.data_out     := U1.io.data_out
}

object ram_rw_a_dff extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ram_rw_a_dff") {
      def top = new ram_rw_a_dff()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
