import chisel3._
import chisel3.util.{log2Ceil, HasBlackBoxPath}
import chisel3.experimental._ // To enable experimental features

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_ram_2r_w_a_dff ==
  *
  * === Abstract ===
  *
  * CW_ram_2r_w_a_dff is a parameterized, asynchronous Flip_Flop based static RAM.
  *
  * Active low 'cs_n' allows data to be written to the RAM. When 'cs_n' is high, the RAM can only read saved data.
  * When the parameter 'rst_mode' is set to '0', the 'rst_n' input is an active low reset to the RAMS, independent of the value of 'cs_n'.
  * When the parameter 'rst_mode' is set to '1', the 'rst_n' input is not used.
  *
  * Data is written into the memory through the 'data_in' port and stored in the address specified by 'wr_addr' on the rising edge of 'wr_n'.
  * 'rd1_addr' & 'rd2_addr' are used to access the address of the data read out through 'data_rd1_out' & 'data_rd2_out' ports.
  * For addresses on 'rd1_addr' & 'rd2_addr' beyond the maximum depth, the 'data_out' buses are driven low.
  * The design may be made scannable by setting the 'test_mode' signal high.
  * In this mode, the 'test_clk' signal is used to capture data into the RAM.
  * When 'test_mode' is high, all RAM addresses are written with the 'data_in' value at the rising edge of test_clk, independent of 'cs_n'.
  * When 'test_mode' and 'scan_enable' are high, data can be shifted into & out of the RAM.
  *
  * === Parameters ===
  *
  * | Parameter     | Legal Range  | Default      | Description    |
  * |---------------|--------------|--------------|----------------|
  * | data_width    | 1 to 256     | 16          | Data width     |
  * | depth         | 2 to 256     | 8           | Depth          |
  * | rst_mode      | 0 or 1       | 0           | Reset mode     |
  *
  * === Ports ===
  *
  * | Name   | Width      | Direction | Description            |
  * |--------|------------|-----------|------------------------|
  * | rst_n  | 1          | Input      | Active low reset       |
  * | cs_n   | 1          | Input      | Active low chip select |
  * | wr_n   | 1          | Input      | Active low write enable |
  * | test_mode | 1          | Input      | Test mode             |
  * | test_clk | 1          | Input      | Test clock             |
  * | rd1_addr | log_depth  | Input      | Address for read 1     |
  * | rd2_addr | log_depth  | Input      | Address for read 2     |
  * | wr_addr  | log_depth  | Input      | Address for write      |
  * | data_in  | data_width | Input      | Data to write         |
  * | data_rd1_out | data_width | Output     | Data read 1           |
  * | data_rd2_out | data_width | Output     | Data read 2           |
  *
  * @param data_width  Data width.
  * @param depth        Depth.
  * @param rst_mode     Reset mode.
  */
class CW_ram_2r_w_a_dff(
  val data_width: Int = 16,
  val depth:      Int = 8,
  val rst_mode:   Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth,
        "rst_mode" -> rst_mode
      )
    )
    with HasBlackBoxPath {

  // Validation of all parameters
  require(data_width >= 1, "data_width must be >= 1")
  require(data_width <= 256, "data_width must be <= 256")
  require(depth >= 2, "depth must be >= 2")
  require(depth <= 256, "depth must be <= 256")
  require(rst_mode == 0 || rst_mode == 1, "rst_mode must be 0 or 1")

  protected val log_depth: Int = log2Ceil(depth)

  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_n:        Bool  = Input(Bool())
    val cs_n:         Bool  = Input(Bool())
    val wr_n:         Bool  = Input(Bool())
    val test_mode:    Bool  = Input(Bool())
    val test_clk:     Clock = Input(Clock())
    val rd1_addr:     UInt  = Input(UInt(log_depth.W))
    val rd2_addr:     UInt  = Input(UInt(log_depth.W))
    val wr_addr:      UInt  = Input(UInt(log_depth.W))
    val data_in:      UInt  = Input(UInt(data_width.W))
    val data_rd1_out: UInt  = Output(UInt(data_width.W))
    val data_rd2_out: UInt  = Output(UInt(data_width.W))
  })
}
