import chisel3._
import circt.stage._
import utest._

class debugger(
  val rd_bits_width: Int = 8,
  val wr_bits_width: Int = 8,
  val clk_freq:      Int = 1,
  val baud_rate:     Int = 19200,
  val mark_parity:   Int = 1)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:             Clock = Input(Clock())
    val reset_n:         Bool  = Input(Bool())
    val div_bypass_mode: Bool  = Input(Bool())
    val rd_bits:         UInt  = Input(UInt(rd_bits_width.W))
    val rxd:             Bool  = Input(Bool())
    val wr_bits:         UInt  = Output(UInt(wr_bits_width.W))
    val txd:             Bool  = Output(Bool())
  })

  protected val U1: CW_debugger = Module(
    new CW_debugger(rd_bits_width, wr_bits_width, clk_freq, baud_rate, mark_parity)
  )
  U1.io.clk             := io.clk
  U1.io.reset_n         := io.reset_n
  U1.io.div_bypass_mode := io.div_bypass_mode
  U1.io.rd_bits         := io.rd_bits
  U1.io.rxd             := io.rxd
  io.wr_bits            := U1.io.wr_bits
  io.txd                := U1.io.txd
}

object debugger extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate debugger") {
      def top = new debugger(8, 8, 1, 19200, 1)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
