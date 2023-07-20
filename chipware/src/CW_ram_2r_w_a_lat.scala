import chisel3._
import chisel3.util.{log2Ceil, HasBlackBoxPath}
import chisel3.experimental._ // To enable experimental features

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_ram_2r_w_a_lat ==
  *
  * === Abstract ===
  *
  * CW_ram_2r_w_a_lat is a parameterizable, asynchronous Latch based static RAM.
  * Active low 'cs_n' allows data to be written to the RAM.
  * When 'cs_n' is high, the RAM can only read saved data.
  * ...
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Range | Default | Description                                  |
  * |--------------|-------------|---------|----------------------------------------------|
  * | data_width   | 1 to 256    | -       | Width of the data bus                        |
  * | depth        | 2 to 256    | -       | Depth of the RAM                              |
  * | rst_mode     | 0, 1        | -       | Reset mode                                   |
  *
  * === Ports ===
  *
  * | Name          | Width      | Direction | Description            |
  * |---------------|------------|-----------|------------------------|
  * | rst_n         | 1          | Input     | Active low reset signal |
  * | cs_n          | 1          | Input     | Chip select signal      |
  * | wr_n          | 1          | Input     | Write enable signal     |
  * | rd1_addr      | log_depth  | Input     | Read address 1          |
  * | rd2_addr      | log_depth  | Input     | Read address 2          |
  * | wr_addr       | log_depth  | Input     | Write address           |
  * | data_in       | data_width | Input     | Data input              |
  * | data_rd1_out  | data_width | Output    | Read data 1 output      |
  * | data_rd2_out  | data_width | Output    | Read data 2 output      |
  *
  * @param data_width Width of the data bus
  * @param depth      Depth of the RAM
  * @param rst_mode   Reset mode
  */
class CW_ram_2r_w_a_lat(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth,
        "rst_mode" -> rst_mode
      )
    )
    with HasBlackBoxPath {

  // Parameter checking
  require(data_width >= 1, "data_width must be >= 1")
  require(data_width <= 256, "data_width must be <= 256")
  require(depth >= 2, "depth must be >= 2")
  require(depth <= 256, "depth must be <= 256")
  require(rst_mode == 0 || rst_mode == 1, "rst_mode must be 0 or 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_n:        Bool = Input(Bool())
    val cs_n:         Bool = Input(Bool())
    val wr_n:         Bool = Input(Bool())
    val rd1_addr:     UInt = Input(UInt(log2Ceil(depth).W))
    val rd2_addr:     UInt = Input(UInt(log2Ceil(depth).W))
    val wr_addr:      UInt = Input(UInt(log2Ceil(depth).W))
    val data_in:      UInt = Input(UInt(data_width.W))
    val data_rd1_out: UInt = Output(UInt(data_width.W))
    val data_rd2_out: UInt = Output(UInt(data_width.W))
  })
}
