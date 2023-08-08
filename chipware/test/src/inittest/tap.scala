import chisel3._
import circt.stage._
import utest._

class tap(
  val width:     Int = 2,
  val id:        Int = 0,
  val version:   Int = 0,
  val part:      Int = 0,
  val man_num:   Int = 0,
  val sync_mode: Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val tck:             Clock = Input(Clock())
    val trst_n:          Bool  = Input(Bool())
    val tms:             Bool  = Input(Bool())
    val tdi:             Bool  = Input(Bool())
    val so:              Bool  = Input(Bool())
    val bypass_sel:      Bool  = Input(Bool())
    val sentinel_val:    UInt  = Input(UInt((width - 1).W))
    val clock_dr:        Bool  = Output(Bool())
    val shift_dr:        Bool  = Output(Bool())
    val update_dr:       Bool  = Output(Bool())
    val tdo:             Bool  = Output(Bool())
    val tdo_en:          Bool  = Output(Bool())
    val tap_state:       UInt  = Output(UInt(16.W))
    val extest:          Bool  = Output(Bool())
    val samp_load:       Bool  = Output(Bool())
    val instructions:    UInt  = Output(UInt(width.W))
    val sync_capture_en: Bool  = Output(Bool())
    val sync_update_dr:  Bool  = Output(Bool())
    val test:            Bool  = Input(Bool())
  })

  protected val U1: CW_tap = Module(new CW_tap(width, id, version, part, man_num, sync_mode))
  U1.io.tck          := io.tck
  U1.io.trst_n       := io.trst_n
  U1.io.tms          := io.tms
  U1.io.tdi          := io.tdi
  U1.io.so           := io.so
  U1.io.bypass_sel   := io.bypass_sel
  U1.io.sentinel_val := io.sentinel_val
  U1.io.test         := io.test
  io.clock_dr        := U1.io.clock_dr
  io.shift_dr        := U1.io.shift_dr
  io.update_dr       := U1.io.update_dr
  io.tdo             := U1.io.tdo
  io.tdo_en          := U1.io.tdo_en
  io.tap_state       := U1.io.tap_state
  io.extest          := U1.io.extest
  io.samp_load       := U1.io.samp_load
  io.instructions    := U1.io.instructions
  io.sync_capture_en := U1.io.sync_capture_en
  io.sync_update_dr  := U1.io.sync_update_dr
}

object tap extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate tapTester") {
      def top = new tap(2, 0, 0, 0, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
