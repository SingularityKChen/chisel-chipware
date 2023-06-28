// filename: CW_ram_rw_s_lat.scala
import chisel3._
import chisel3.experimental._ // To enable experimental features
import chisel3.util._

/**
  * == CW_ram_rw_s_lat ==
  *
  * === Abstract ===
  *
  * CW_ram_rw_s_lat is a LATCH based synchronous, parameterized, single port, static RAM.
  *
  * When 'cs_n' is high, the 'data_out' bus is driven low.
  * For addresses beyond the maximum depth, the 'data_out' bus is driven low.
  * 'data_out' equals 'data_in' when 'cs_n', 'wr_n' & 'clk' are low.
  * Data is captured into the memory cell on the rising edge of 'clk'.
  *
  * === Parameters ===
  *
  * | Parameter   | Legal Range      | Description                                                                 |
  * |-------------|------------------|-----------------------------------------------------------------------------|
  * | data_width  | 1 <= data_width  | Data width of the RAM                                                       |
  * | depth       | 2 <= depth <= 256| Depth of the RAM                                                             |
  *
  * === Ports ===
  *
  * | Name       | Width                 | Direction | Description                 |
  * |------------|-----------------------|-----------|-----------------------------|
  * | clk        | 1 bit                 | Input     | Clock signal                |
  * | cs_n       | 1 bit                 | Input     | Chip select signal (active low) |
  * | wr_n       | 1 bit                 | Input     | Write enable signal (active low) |
  * | rw_addr    | log2(depth) bits      | Input     | Read/write address          |
  * | data_in    | data_width bits       | Input     | Input data                  |
  * | data_out   | data_width bits       | Output    | Output data                 |
  *
  * @param data_width Data width of the RAM
  * @param depth Depth of the RAM
  */
class CW_ram_rw_s_lat(val data_width: Int = 16, val depth: Int = 8)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth
      )
    ) {
  // Validation of all parameters
  require(data_width >= 1, "data_width must be >= 1")
  require(depth >= 2 && depth <= 256, "depth should be in the range [2, 256]")

  // Declare ports with type annotations
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock()) // Clock signal
    val cs_n:     Bool  = Input(Bool()) // Chip select signal (active low)
    val wr_n:     Bool  = Input(Bool()) // Write enable signal (active low)
    val rw_addr:  UInt  = Input(UInt(log2Ceil(depth).W)) // Read/write address
    val data_in:  UInt  = Input(UInt(data_width.W)) // Input data
    val data_out: UInt  = Output(UInt(data_width.W)) // Output data
  })
}
