import chisel3._
import chisel3.util._

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_ram_rw_a_lat ==
  *
  * === Abstract ===
  *
  * CW_ram_rw_a_lat is a LATCH based asynchronous, parameterized, single port, static RAM.
  * When 'cs_n' is high, the 'data_out' bus is driven low.
  * For addresses beyond the maximum depth, the 'data_out' bus is driven low.
  * 'data_out' equals 'data_in' when 'cs_n' & 'wr_n' are low.
  * Data is captured into the memory cell on the rising edge of 'wr_n'.
  * When the parameter 'rst_mode' is set to '0', the 'rst_n' input is an active low reset to the RAMS, independent of the value of 'cs_n'.
  * When the parameter 'rst_mode' is set to '1', the 'rst_n' input is not used.
  *
  * === Parameters ===
  *
  * | Parameter   | Legal Range | Default | Description                |
  * |-------------|-------------|---------|----------------------------|
  * | data_width  | 1 to 256    | -       | Width of data bus          |
  * | depth       | 2 to 256    | -       | Depth of RAM               |
  * | rst_mode    | 0, 1        | -       | Reset mode                 |
  *
  * === Ports ===
  *
  * | Name      | Width                    | Direction | Description          |
  * |-----------|--------------------------|-----------|----------------------|
  * | rst_n     | 1                        | Input     | Active low reset      |
  * | cs_n      | 1                        | Input     | Chip select           |
  * | wr_n      | 1                        | Input     | Write enable          |
  * | rw_addr   | log_depth (calculated)   | Input     | Address for read/write |
  * | data_in   | data_width               | Input     | Input data            |
  * | data_out  | data_width               | Output    | Output data           |
  *
  * @param data_width Width of data bus
  * @param depth Depth of RAM
  * @param rst_mode Reset mode
  */
class CW_ram_rw_a_lat(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth,
        "rst_mode" -> rst_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(data_width >= 1 && data_width <= 256, "data_width must be in the range [1, 256]")
  require(depth >= 2 && depth <= 256, "depth must be in the range [2, 256]")
  require(rst_mode == 0 || rst_mode == 1, "rst_mode should be 0 or 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_n:    Bool = Input(Bool()) // Active low reset
    val cs_n:     Bool = Input(Bool()) // Chip select
    val wr_n:     Bool = Input(Bool()) // Write enable
    val rw_addr:  UInt = Input(UInt((log2Ceil(depth).W))) // Address for read/write
    val data_in:  UInt = Input(UInt(data_width.W)) // Input data
    val data_out: UInt = Output(UInt(data_width.W)) // Output data
  })
}
