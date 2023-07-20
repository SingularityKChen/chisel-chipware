// filename: CW_8b10b_dec.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_8b10b_dec ==
  *
  * === Abstract ===
  *
  * CW_8b10b_dec decodes 1 to 16 bytes of data using the 8b10b Direct Current (DC)balanced encoding scheme developed at IBM.
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Range | Default | Description                                                                                                     |
  * |--------------|-------------|---------|-----------------------------------------------------------------------------------------------------------------|
  * | bytes        | 1 to 16     | 2       | Number of bytes to encode                                                                                       |
  * | k28_5_only   | 0 or 1      | 0       | Special Character subset control parameter                                                                     |
  * | en_mode      | 0 or 1      | 0       | Enable control                                                                                                  |
  * | init_mode    | 0 or 1      | 0       | Initialization mode for running disparity                                                                       |
  * | rst_mode     | 0 or 1      | 0       | Reset mode                                                                                                      |
  * | op_iso_mode  | -           | 0       |                                                                                                                 |
  *
  * === Ports ===
  *
  * | Name         | Width              | Direction | Description                                                                                                |
  * |--------------|--------------------|-----------|------------------------------------------------------------------------------------------------------------|
  * | clk          | 1 bit              | Input     | Clock input                                                                                                |
  * | rst_n        | 1 bit              | Input     | Asynchronous reset input, active low                                                                        |
  * | init_rd_n    | 1 bit              | Input     | Synchronous initialization control input, active low                                                        |
  * | init_rd_val  | 1 bit              | Input     | Value of initial Running Disparity                                                                          |
  * | data_in      | bytes × 10 bit(s)  | Input     | Input 8b/10b data for decoding                                                                              |
  * | error        | 1 bit              | Output    | Active high, error flag indicating the presence of any type of error (running disparity or coding)          |
  * | rd           | 1 bit              | Output    | Current Running Disparity (after decoding data presented at data_in to data_out)                            |
  * | k_char       | bytes bit(s)       | Output    | Special Character indicators (one indicator per decoded byte)                                              |
  * | data_out     | bytes × 8 bit(s)   | Output    | Decoded output data                                                                                         |
  * | rd_err       | 1 bit              | Output    | Active high, error flag indicating the presence of one or more Running Disparity errors in the data_out      |
  * | code_err     | 1 bit              | Output    | Active high, error flag indicating the presence of a coding error in at least one byte of the data_out       |
  * | enable       | 1 bit              | Input     | Enables register clocking                                                                                   |
  * | rd_err_bus   | bytes bit(s)       | Output    | This bus indicates which bytes of the incoming bus have Running Disparity errors (one bit per incoming data) |
  * | code_err_bus | bytes bit(s)       | Output    | This bus indicates which bytes of the incoming bus have Coding errors (one bit per incoming data)            |
  *
  * @param bytes       Number of bytes to encode
  * @param k28_5_only  Special Character subset control parameter
  * @param en_mode     Enable control
  * @param init_mode   Initialization mode for running disparity
  * @param rst_mode    Reset mode
  * @param op_iso_mode -
  */
class CW_8b10b_dec(
  val bytes:       Int = 2,
  val k28_5_only:  Int = 0,
  val en_mode:     Int = 0,
  val init_mode:   Int = 0,
  val rst_mode:    Int = 0,
  val op_iso_mode: Int = 0)
    extends BlackBox(
      Map(
        "bytes" -> bytes,
        "k28_5_only" -> k28_5_only,
        "en_mode" -> en_mode,
        "init_mode" -> init_mode,
        "rst_mode" -> rst_mode,
        "op_iso_mode" -> op_iso_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(bytes >= 1 && bytes <= 16, s"bytes must be between 1 and 16 (inclusive), got: $bytes")
  require(k28_5_only >= 0 && k28_5_only <= 1, s"k28_5_only must be either 0 or 1, got: $k28_5_only")
  require(en_mode >= 0 && en_mode <= 1, s"en_mode must be either 0 or 1, got: $en_mode")
  require(init_mode >= 0 && init_mode <= 1, s"init_mode must be either 0 or 1, got: $init_mode")
  require(rst_mode >= 0 && rst_mode <= 1, s"rst_mode must be either 0 or 1, got: $rst_mode")
  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val init_rd_n:    Bool  = Input(Bool())
    val init_rd_val:  Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt((bytes * 10).W))
    val error:        Bool  = Output(Bool())
    val rd:           Bool  = Output(Bool())
    val k_char:       UInt  = Output(UInt(bytes.W))
    val data_out:     UInt  = Output(UInt((bytes * 8).W))
    val rd_err:       Bool  = Output(Bool())
    val code_err:     Bool  = Output(Bool())
    val enable:       Bool  = Input(Bool())
    val rd_err_bus:   UInt  = Output(UInt(bytes.W))
    val code_err_bus: UInt  = Output(UInt(bytes.W))
  })
}
