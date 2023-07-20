import chisel3._
import circt.stage._
import utest._

class shifter(val data_width: Int = 8, val sh_width: Int = 3, val inv_mode: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val data_in:  UInt = Input(UInt(data_width.W))
    val data_tc:  Bool = Input(Bool())
    val sh:       UInt = Input(UInt(sh_width.W))
    val sh_tc:    Bool = Input(Bool())
    val sh_mode:  Bool = Input(Bool())
    val data_out: UInt = Output(UInt(data_width.W))
  })

  protected val U1: CW_shifter = Module(new CW_shifter(data_width, sh_width, inv_mode))
  U1.io.data_in := io.data_in
  U1.io.data_tc := io.data_tc
  U1.io.sh      := io.sh
  U1.io.sh_tc   := io.sh_tc
  U1.io.sh_mode := io.sh_mode
  io.data_out   := U1.io.data_out
}

object shifter extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate shifter") {
      def top = new shifter(8, 3, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
