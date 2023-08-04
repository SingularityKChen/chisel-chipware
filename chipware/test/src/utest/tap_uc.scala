import chisel3._
import circt.stage._
import utest._

class tap_uc(
  val width:         Int = 2,
  val id:            Int = 0,
  val idcode_opcode: Int = 1,
  val version:       Int = 0,
  val part:          Int = 0,
  val man_num:       Int = 0,
  val sync_mode:     Int = 0,
  val tst_mode:      Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val tck:           Bool = Input(Bool()) // Test clock
    val trst_n:        Bool = Input(Bool()) // Test reset, active low
    val tms:           Bool = Input(Bool()) // Test mode select
    val tdi:           Bool = Input(Bool()) // Test data in
    val so:            Bool = Input(Bool()) // Serial data from boundary scan registers and data registers
    val bypass_sel:    Bool = Input(Bool()) // Selects bypass register, active high
    val sentinel_val:  UInt = Input(UInt((width - 2).W)) // User defined status bits
    val ver_sel:       Bool = Input(Bool()) // Selects version from parameter or ver input port
    val device_id_sel: Bool = Input(Bool()) // Selects device identification register, active high
    val user_code_sel: Bool =
      Input(Bool()) // Selects user_code_val bus for input into device identification register, active high
    val user_code_val:   UInt = Input(UInt(32.W)) // 32-bit user defined code
    val ver:             UInt = Input(UInt(4.W)) // 4-bit version number
    val part_num:        UInt = Input(UInt(16.W)) // 16-bit part number
    val part_num_sel:    Bool = Input(Bool()) // Selects part from parameter or part_num from input port
    val mnfr_id:         UInt = Input(UInt(11.W)) // 11-bit JEDEC manufacturer's identity code (mnfr_id != 127)
    val mnfr_id_sel:     Bool = Input(Bool()) // Selects man_num from parameter or mnfr_id from input port
    val test:            Bool = Input(Bool()) // Scantest enable pin, NOT used in CW
    val clock_dr:        Bool = Output(Bool()) // Clock's in data in asynchronous mode
    val shift_dr:        Bool = Output(Bool()) // Enables shifting of data in both synchronous and asynchronous mode
    val update_dr:       Bool = Output(Bool()) // Enables updating data in asynchronous mode
    val tdo:             Bool = Output(Bool()) // Test data out
    val tdo_en:          Bool = Output(Bool()) // Enables for tdo output buffer
    val tap_state:       UInt = Output(UInt(16.W)) // Current state of the TAP finite state machine
    val instructions:    UInt = Output(UInt(width.W)) // Instruction register output
    val sync_capture_en: Bool = Output(Bool()) // Enable for synchronous capture
    val sync_update_dr:  Bool = Output(Bool()) // Enable updating new data in synchronous mode
  })

  // Define the BlackBox instantiation
  protected val U1: CW_tap_uc = Module(
    new CW_tap_uc(width, id, idcode_opcode, version, part, man_num, sync_mode, tst_mode)
  )
  U1.io.tck           := io.tck
  U1.io.trst_n        := io.trst_n
  U1.io.tms           := io.tms
  U1.io.tdi           := io.tdi
  U1.io.so            := io.so
  U1.io.bypass_sel    := io.bypass_sel
  U1.io.sentinel_val  := io.sentinel_val
  U1.io.ver_sel       := io.ver_sel
  U1.io.device_id_sel := io.device_id_sel
  U1.io.user_code_sel := io.user_code_sel
  U1.io.user_code_val := io.user_code_val
  U1.io.ver           := io.ver
  U1.io.part_num      := io.part_num
  U1.io.part_num_sel  := io.part_num_sel
  U1.io.mnfr_id       := io.mnfr_id
  U1.io.mnfr_id_sel   := io.mnfr_id_sel
  U1.io.test          := io.test
  io.clock_dr         := U1.io.clock_dr
  io.shift_dr         := U1.io.shift_dr
  io.update_dr        := U1.io.update_dr
  io.tdo              := U1.io.tdo
  io.tdo_en           := U1.io.tdo_en
  io.tap_state        := U1.io.tap_state
  io.instructions     := U1.io.instructions
  io.sync_capture_en  := U1.io.sync_capture_en
  io.sync_update_dr   := U1.io.sync_update_dr
}

object tap_uc extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate tap_uc") {
      def top = new tap_uc(2, 1, 1, 0, 0, 0, 0, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
