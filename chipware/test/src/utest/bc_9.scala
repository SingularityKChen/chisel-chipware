// filename: bc_9.scala
import chisel3._
import circt.stage._
import utest._

class bc_9 extends RawModule {
  val io = IO(new Bundle {
    val capture_clk: Clock = Input(Clock())
    val update_clk:  Clock = Input(Clock())
    val capture_en:  Bool  = Input(Bool())
    val update_en:   Bool  = Input(Bool())
    val shift_dr:    Bool  = Input(Bool())
    val mode1:       Bool  = Input(Bool())
    val mode2:       Bool  = Input(Bool())
    val si:          Bool  = Input(Bool())
    val pin_input:   Bool  = Input(Bool())
    val output_data: Bool  = Input(Bool())
    val data_out:    Bool  = Output(Bool())
    val so:          Bool  = Output(Bool())
  })

  protected val U1: CW_bc_9 = Module(new CW_bc_9())

  U1.io.capture_clk := io.capture_clk
  U1.io.update_clk  := io.update_clk
  U1.io.capture_en  := io.capture_en
  U1.io.update_en   := io.update_en
  U1.io.shift_dr    := io.shift_dr
  U1.io.mode1       := io.mode1
  U1.io.mode2       := io.mode2
  U1.io.si          := io.si
  U1.io.pin_input   := io.pin_input
  U1.io.output_data := io.output_data
  io.data_out       := U1.io.data_out
  io.so             := U1.io.so
}

object bc_9 extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate bc_9") {
      def top = new bc_9()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
