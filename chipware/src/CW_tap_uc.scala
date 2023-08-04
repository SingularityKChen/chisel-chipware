import chisel3._
import chisel3.util._
import chisel3.experimental._

/**
  * == CW_tap_uc ==
  *
  * === Abstract ===
  *
  * TAP Controller with Usercode support.
  *
  * === Parameters ===
  *
  * | Parameter      | Legal Range                  | Default  | Description                             |
  * |----------------|------------------------------|----------|-----------------------------------------|
  * | width          | 2 to 32                      | 2        | Width of instruction register          |
  * | id             | 0 or 1                       | 0        | Determines presence of ID register     |
  * | idcode_opcode  | 1 to ((2<sup>(width)</sup>)-1) | 1        | Opcode for IDCODE instruction          |
  * | version        | 0 to 15                      | 0        | 4-bit version number                   |
  * | part           | 0 to 65535                   | 0        | 16-bit part number                     |
  * | man_num        | 0 to 2047, man_num != 127    | 0        | 11-bit JEDEC manufacturer identity code|
  * | sync_mode      | 0 or 1                       | 0        | Synchronous mode for registers         |
  * | tst_mode       | 0 or 1                       | 0        | Test pin control                        |
  *
  * === Ports ===
  *
  * | Name           | Width                        | Direction | Description                             |
  * |----------------|------------------------------|-----------|-----------------------------------------|
  * | tck            | 1 bit                        | Input     | Test clock                              |
  * | trst_n         | 1 bit                        | Input     | Test reset, active low                  |
  * | tms            | 1 bit                        | Input     | Test mode select                        |
  * | tdi            | 1 bit                        | Input     | Test data in                            |
  * | so             | 1 bit                        | Input     | Serial data from boundary scan registers and data registers |
  * | bypass_sel     | 1 bit                        | Input     | Selects bypass register, active high   |
  * | sentinel_val   | width - 2 (s) bits           | Input     | User defined status bits                |
  * | ver_sel        | 1 bit                        | Input     | Selects version from parameter or ver input port |
  * | device_id_sel  | 1 bit                        | Input     | Selects device identification register, active high |
  * | user_code_sel  | 1 bit                        | Input     | Selects user_code_val bus for input into device identification register, active high |
  * | user_code_val  | 32 bits                      | Input     | 32-bit user defined code                |
  * | ver            | 4 bits                       | Input     | 4-bit version number                    |
  * | part_num       | 16 bits                      | Input     | 16-bit part number                      |
  * | part_num_sel   | 1 bit                        | Input     | Selects part from parameter or part_num from input port |
  * | mnfr_id        | 11 bits                      | Input     | 11-bit JEDEC manufacturer's identity code (mnfr_id != 127) |
  * | mnfr_id_sel    | 1 bit                        | Input     | Selects man_num from parameter or mnfr_id from input port |
  * | test           | 1 bit                        | Input     | Scantest enable pin, NOT used in CW     |
  * | clock_dr       | 1 bit                        | Output    | Clock's in data in asynchronous mode    |
  * | shift_dr       | 1 bit                        | Output    | Enables shifting of data in both synchronous and asynchronous mode |
  * | update_dr      | 1 bit                        | Output    | Enables updating data in asynchronous mode |
  * | tdo            | 1 bit                        | Output    | Test data out                           |
  * | tdo_en         | 1 bit                        | Output    | Enables for tdo output buffer           |
  * | tap_state      | 16 bits                      | Output    | Current state of the TAP finite state machine |
  * | instructions   | width bit(s)                 | Output    | Instruction register output             |
  * | sync_capture_en | 1 bit                        | Output    | Enable for synchronous capture          |
  * | sync_update_dr | 1 bit                        | Output    | Enable updating new data in synchronous mode |
  *
  * @param width        Width of instruction register
  * @param id           Determines presence of ID register
  * @param idcode_opcode Opcode for IDCODE instruction
  * @param version      4-bit version number
  * @param part         16-bit part number
  * @param man_num      11-bit JEDEC manufacturer identity code
  * @param sync_mode    Synchronous mode for registers
  * @param tst_mode     Test pin control
  */
class CW_tap_uc(
  val width:         Int = 2,
  val id:            Int = 0,
  val idcode_opcode: Int = 1,
  val version:       Int = 0,
  val part:          Int = 0,
  val man_num:       Int = 0,
  val sync_mode:     Int = 0,
  val tst_mode:      Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "id" -> id,
        "idcode_opcode" -> idcode_opcode,
        "version" -> version,
        "part" -> part,
        "man_num" -> man_num,
        "sync_mode" -> sync_mode,
        "tst_mode" -> tst_mode
      )
    )
    with HasBlackBoxPath {
  // Add validations for parameters
  require(width >= 2 && width <= 32, "width must be between 2 and 32")
  require(id == 0 || id == 1, "id must be 0 or 1")
  require(idcode_opcode >= 1 && idcode_opcode < Math.pow(2, width), "idcode_opcode out of range")
  require(version >= 0 && version <= 15, "version must be between 0 and 15")
  require(part >= 0 && part <= 65535, "part must be between 0 and 65535")
  require(man_num >= 0 && man_num <= 2047 && man_num != 127, "man_num must be between 0 and 2047 (excluding 127)")
  require(sync_mode == 0 || sync_mode == 1, "sync_mode must be 0 or 1")
  require(tst_mode == 0 || tst_mode == 1, "tst_mode must be 0 or 1")

  // Define ports with type annotations
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
}
