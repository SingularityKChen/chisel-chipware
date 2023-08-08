import chisel3._
import circt.stage._
import utest._

class crc_s(
  val data_width: Int = 16,
  val poly_size:  Int = 16,
  val crc_cfg:    Int = 7,
  val bit_order:  Int = 3,
  val poly_coef0: Int = 4129,
  val poly_coef1: Int = 0,
  val poly_coef2: Int = 0,
  val poly_coef3: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val clk:        Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val init_n:     Bool  = Input(Bool())
    val enable:     Bool  = Input(Bool())
    val drain:      Bool  = Input(Bool())
    val ld_crc_n:   Bool  = Input(Bool())
    val data_in:    UInt  = Input(UInt(data_width.W))
    val crc_in:     UInt  = Input(UInt(poly_size.W))
    val draining:   Bool  = Output(Bool())
    val drain_done: Bool  = Output(Bool())
    val crc_ok:     Bool  = Output(Bool())
    val data_out:   UInt  = Output(UInt(data_width.W))
    val crc_out:    UInt  = Output(UInt(poly_size.W))
  })
  protected val U1: CW_crc_s = Module(
    new CW_crc_s(data_width, poly_size, crc_cfg, bit_order, poly_coef0, poly_coef1, poly_coef2, poly_coef3)
  )
  U1.io.clk      := io.clk
  U1.io.rst_n    := io.rst_n
  U1.io.init_n   := io.init_n
  U1.io.enable   := io.enable
  U1.io.drain    := io.drain
  U1.io.ld_crc_n := io.ld_crc_n
  U1.io.data_in  := io.data_in
  U1.io.crc_in   := io.crc_in
  io.draining    := U1.io.draining
  io.drain_done  := U1.io.drain_done
  io.crc_ok      := U1.io.crc_ok
  io.data_out    := U1.io.data_out
  io.crc_out     := U1.io.crc_out

}

object crc_s extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate crc_s") {
      def top = new crc_s(16, 16, 7, 3, 4129, 0, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
