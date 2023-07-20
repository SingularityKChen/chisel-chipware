import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_8b10b_enc ==
  *
  * === Abstract ===
  *
  * CW_8b10b_enc encodes 1 to 16 bytes of data using the 8b10b Direct Current (DC) balanced encoding scheme.
  *
  * === Parameters ===
  *
  * | Parameter    | Values | Description                                                                             |
  * |--------------|--------|-----------------------------------------------------------------------------------------|
  * | bytes        | 1 to 16 | Number of bytes to encode                                                               |
  * | k28_5_only   | 0 or 1 | Special Character subset control parameter                                             |
  * | en_mode      | 0 or 1 | Enable control                                                                          |
  * | init_mode    | 0 or 1 | Initialization mode for running disparity                                              |
  * | rst_mode     | 0 or 1 | Reset mode                                                                              |
  * | op_iso_mode  | 0      | Op isolation mode                                                                       |
  *
  * === Ports ===
  *
  * | Name         | Width               | Direction | Description                                     |
  * |--------------|---------------------|-----------|-------------------------------------------------|
  * | clk          | 1 bit               | Input     | Clock                                           |
  * | rst_n        | 1 bit               | Input     | Asynchronous reset active low                   |
  * | init_rd_n    | 1 bit               | Input     | Synchronous initialization, active low           |
  * | init_rd_val  | 1 bit               | Input     | Value of initial Running Disparity               |
  * | k_char       | bytes bit(s)        | Input     | Special Character controls                       |
  * | data_in      | bytes × 8 bit(s)    | Input     | Input data for encoding                          |
  * | enable       | 1 bit               | Input     | Enables register clocking                        |
  * | rd           | 1 bit               | Output    | Current Running Disparity (before encoding data) |
  * | data_out     | bytes × 10 bit(s)   | Output    | 8b10b Encoded data                              |
  *
  * @param bytes       Number of bytes to encode
  * @param k28_5_only  Special Character subset control parameter
  * @param en_mode     Enable control
  * @param init_mode   Initialization mode for running disparity
  * @param rst_mode    Reset mode
  * @param op_iso_mode Op isolation mode
  */
class CW_8b10b_enc(
  val bytes:       Int = 2,
  val k28_5_only:  Int = 0,
  val en_mode:     Int = 0,
  val init_mode:   Int = 0,
  val rst_mode:    Int = 0,
  val op_iso_mode: Int = 0)
    extends BlackBox(
      Map(
        "bytes" -> IntParam(bytes),
        "k28_5_only" -> IntParam(k28_5_only),
        "en_mode" -> IntParam(en_mode),
        "init_mode" -> IntParam(init_mode),
        "rst_mode" -> IntParam(rst_mode),
        "op_iso_mode" -> IntParam(op_iso_mode)
      )
    )
    with HasBlackBoxPath {
  val io = IO(new Bundle {
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val init_rd_n:   Bool  = Input(Bool())
    val init_rd_val: Bool  = Input(Bool())
    val k_char:      UInt  = Input(UInt(bytes.W))
    val data_in:     UInt  = Input(UInt((bytes * 8).W))
    val enable:      Bool  = Input(Bool())
    val rd:          Bool  = Output(Bool())
    val data_out:    UInt  = Output(UInt((bytes * 10).W))
  })
}
