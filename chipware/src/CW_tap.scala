import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_tap ==
  *
  * === Abstract ===
  *
  * TAP Controller
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | width      | 2 to 32      | 2        | Width of instruction register |
  * | id         | 0 or 1       | 0        | Determines whether the device identification register is present. 0 = not present, 1 = present |
  * | version    | 0 to 15      | 0        | 4-bit version number |
  * | part       | 0 to 65535   | 0        | 16-bit part number |
  * | man_num    | 0 to 2047, man_num !=127 | 0 | 11-bit JEDEC manufacturer identity code |
  * | sync_mode  | 0 or 1       | 0        | Determines whether the bypass, device identification, and instruction registers are synchronous with respect to tck. 0 = asynchronous, 1 = synchronous |
  *
  * === Ports ===
  *
  * | Name           | Width        | Direction | Description  |
  * |----------------|--------------|-----------|--------------|
  * | tck            | 1 bit        | Input     | Test clock |
  * | trst_n         | 1 bit        | Input     | Test reset, active low |
  * | tms            | 1 bit        | Input     | Test mode select |
  * | tdi            | 1 bit        | Input     | Test data in |
  * | so             | 1 bit        | Input     | Serial data from boundary scan registers and data registers |
  * | bypass_sel     | 1 bit        | Input     | Selects bypass register, active high |
  * | sentinel_val   | width-1 bit(s) | Input   | User defined status bits |
  * | clock_dr       | 1 bit        | Output    | Clock's in data in asynchronous mode |
  * | shift_dr       | 1 bit        | Output    | Enables shifting of data in both synchronous and asynchronous mode |
  * | update_dr      | 1 bit        | Output    | Enables updating data in asynchronous mode |
  * | tdo            | 1 bit        | Output    | Test data out |
  * | tdo_en         | 1 bit        | Output    | Enables for tdo output buffer |
  * | tap_state      | 16 bits      | Output    | Current state of the TAP finite state machine |
  * | extest         | 1 bit        | Output    | EXTEST decoded instruction |
  * | samp_load      | 1 bit        | Output    | SAMPLE/PRELOAD decoded instruction |
  * | instructions   | width bit(s) | Output    | Instruction register output |
  * | sync_capture_en | 1 bit       | Output    | Enable for synchronous capture |
  * | sync_update_dr | 1 bit        | Output    | Enable updating new data in |
  * | test           | 1 bit        | Input     | For scannable designs, the test_mode pin is held active (HIGH) during testing. For normal operation , it is held inactive (LOW) |
  *
  * @param width     Width of instruction register
  * @param id        Determines whether the device identification register is present. 0 = not present, 1 = present
  * @param version   4-bit version number
  * @param part      16-bit part number
  * @param man_num   11-bit JEDEC manufacturer identity code
  * @param sync_mode Determines whether the bypass, device identification, and instruction registers are synchronous with respect to tck. 0 = asynchronous, 1 = synchronous
  */
class CW_tap(
  val width:     Int = 2,
  val id:        Int = 0,
  val version:   Int = 0,
  val part:      Int = 0,
  val man_num:   Int = 0,
  val sync_mode: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "id" -> id,
        "version" -> version,
        "part" -> part,
        "man_num" -> man_num,
        "sync_mode" -> sync_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 2 && width <= 32, "width must be in the range [2, 32]")
  require(id == 0 || id == 1, "id must be 0 or 1")
  require(version >= 0 && version <= 15, "version must be in the range [0, 15]")
  require(part >= 0 && part <= 65535, "part must be in the range [0, 65535]")
  require(
    man_num >= 0 && man_num <= 2047 && man_num != 127,
    "man_num must be in the range [0, 2047] and not equal to 127"
  )
  require(sync_mode == 0 || sync_mode == 1, "sync_mode must be 0 or 1")

  // Define ports with type annotations
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
}
