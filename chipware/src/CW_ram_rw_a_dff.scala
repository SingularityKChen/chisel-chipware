import chisel3._
import chisel3.experimental._
import chisel3.util.log2Ceil

/**
  * == CW_ram_rw_a_dff ==
  *
  * === Abstract ===
  *
  * Asynchronous single port static RAM (Flip-Flop Based)
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range | Default | Description                                |
  * |------------|-------------|---------|--------------------------------------------|
  * | data_width | 1 to 256    |         | Width of the RAM data                       |
  * | depth      | 2 to 256    |         | Depth of the RAM                            |
  * | rst_mode   | 0 to 1      |         | Reset mode (0: synchronous, 1: asynchronous) |
  *
  * === Ports ===
  *
  * | Name       | Width                   | Direction | Description                                |
  * |------------|-------------------------|-----------|--------------------------------------------|
  * | rst_n      | 1                       | Input     | Active-low reset signal                     |
  * | cs_n       | 1                       | Input     | Chip select signal                          |
  * | wr_n       | 1                       | Input     | Write enable signal                         |
  * | test_mode  | 1                       | Input     | Test mode enable signal                     |
  * | test_clk   | 1                       | Input     | Test clock signal                           |
  * | rw_addr    | log2Ceil(depth)         | Input     | RAM address for read/write operations       |
  * | data_in    | data_width              | Input     | Data input for write operations             |
  * | data_out   | data_width              | Output    | Data output for read operations             |
  *
  * @param data_width Width of the RAM data
  * @param depth      Depth of the RAM
  * @param rst_mode   Reset mode (0: synchronous, 1: asynchronous)
  */
class CW_ram_rw_a_dff(val data_width: Int, val depth: Int, val rst_mode: Int)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth,
        "rst_mode" -> rst_mode
      )
    ) {
  // Validations for parameters
  require(data_width >= 1 && data_width <= 256, "data_width must be in the range of 1 to 256")
  require(depth >= 2 && depth <= 256, "depth must be in the range of 2 to 256")
  require(rst_mode >= 0 && rst_mode <= 1, "rst_mode must be either 0 or 1")

  // Declare ports
  val io = IO(new Bundle {
    val rst_n:     Bool  = Input(Bool())
    val cs_n:      Bool  = Input(Bool())
    val wr_n:      Bool  = Input(Bool())
    val test_mode: Bool  = Input(Bool())
    val test_clk:  Clock = Input(Clock())
    val rw_addr:   UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:   UInt  = Input(UInt(data_width.W))
    val data_out:  UInt  = Output(UInt(data_width.W))
  })
}
