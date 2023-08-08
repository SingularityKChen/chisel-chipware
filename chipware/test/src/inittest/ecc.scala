import chisel3._
import circt.stage._
import utest._

class ecc(val width: Int = 32, val chkbits: Int = 7, val synd_sel: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val gen:        Bool = Input(Bool())
    val correct_n:  Bool = Input(Bool())
    val datain:     UInt = Input(UInt(width.W))
    val chkin:      UInt = Input(UInt(chkbits.W))
    val err_detect: Bool = Output(Bool())
    val err_multpl: Bool = Output(Bool())
    val dataout:    UInt = Output(UInt(width.W))
    val chkout:     UInt = Output(UInt(chkbits.W))
  })
  protected val U1: CW_ecc = Module(new CW_ecc(width, chkbits, synd_sel))
  U1.io.gen       := io.gen
  U1.io.correct_n := io.correct_n
  U1.io.datain    := io.datain
  U1.io.chkin     := io.chkin
  io.err_detect   := U1.io.err_detect
  io.err_multpl   := U1.io.err_multpl
  io.dataout      := U1.io.dataout
  io.chkout       := U1.io.chkout
}

object ecc extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ecc") {
      def top = new ecc()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
