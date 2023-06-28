// filename: CW_ram_r_w_a_dff.scala
import chisel3._
import chisel3.util.log2Ceil
import chisel3.experimental._

/**
  * == CW_ram_r_w_a_dff ==
  *
  * === Abstract ===
  *
  * Asynchronous dual-port static RAM (Flip-Flop Based)
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Description           |
  * |------------|--------------|-----------------------|
  * | data_width | >= 1, <= 256 | Width of the data bus |
  * | depth      | >= 2, <= 256 | Depth of the RAM       |
  * | rst_mode   | 0, 1         | Reset mode             |
  *
  * === Ports ===
  *
  * | Name       | Width  | Direction | Description              |
  * |------------|--------|-----------|--------------------------|
  * | rst_n      | 1      | Input     | Reset signal (active low) |
  * | cs_n       | 1      | Input     | Chip select (active low)  |
  * | wr_n       | 1      | Input     | Write enable (active low) |
  * | test_mode  | 1      | Input     | Test mode enable         |
  * | test_clk   | 1      | Input     | Test clock               |
  * | rd_addr    | log2Ceil(depth) | Input | Read address       |
  * | wr_addr    | log2Ceil(depth) | Input | Write address      |
  * | data_in    | data_width     | Input | Data input          |
  * | data_out   | data_width     | Output | Data output        |
  *
  * @param data_width Width of the data bus
  * @param depth      Depth of the RAM
  * @param rst_mode   Reset mode (0: asynchronous, 1: synchronous)
  */
class CW_ram_r_w_a_dff(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth,
        "rst_mode" -> rst_mode
      )
    ) {
  // Validations
  require(data_width >= 1, "data_width must be >= 1")
  require(data_width <= 256, "data_width should be <= 256")
  require(depth >= 2, "depth must be >= 2")
  require(depth <= 256, "depth should be <= 256")
  require(rst_mode >= 0 && rst_mode <= 1, "rst_mode must be in the range [0, 1]")

  // Define ports
  val io = IO(new Bundle {
    val rst_n:     Bool = Input(Bool())
    val cs_n:      Bool = Input(Bool())
    val wr_n:      Bool = Input(Bool())
    val test_mode: Bool = Input(Bool())
    val test_clk:  Bool = Input(Bool())
    val rd_addr:   UInt = Input(UInt(log2Ceil(depth).W))
    val wr_addr:   UInt = Input(UInt(log2Ceil(depth).W))
    val data_in:   UInt = Input(UInt(data_width.W))
    val data_out:  UInt = Output(UInt(data_width.W))
  })
}
