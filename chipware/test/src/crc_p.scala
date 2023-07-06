import chisel3._
import circt.stage._
import utest._

class crc_p(
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
    val data_in: UInt = Input(UInt(data_width.W))
    val crc_in:  UInt = Input(UInt(poly_size.W))
    val crc_ok:  Bool = Output(Bool())
    val crc_out: UInt = Output(UInt(poly_size.W))
  })

  protected val U1: CW_crc_p = Module(
    new CW_crc_p(data_width, poly_size, crc_cfg, bit_order, poly_coef0, poly_coef1, poly_coef2, poly_coef3)
  )
  U1.io.data_in := io.data_in
  U1.io.crc_in  := io.crc_in
  io.crc_ok     := U1.io.crc_ok
  io.crc_out    := U1.io.crc_out
}

object crc_p extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate crc_p") {
      def top = new crc_p(16, 16, 7, 3, 4129, 0, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
