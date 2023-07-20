// filename: CW_ram_r_w_s_lat.scala
import chisel3._
import chisel3.experimental._
import chisel3.util._

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_ram_r_w_s_lat ==
  *
  * === Abstract ===
  *
  * CW_ram_r_w_s_lat is a LATCH-based synchronous, parameterized, dual-port, static RAM.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range | Default | Description       |
  * |------------|-------------|---------|-------------------|
  * | data_width | 1 <= n <= 256 | 16 | Width of the RAM data |
  * | depth      | 2 <= n <= 256 | 8 | Depth of the RAM     |
  *
  * === Ports ===
  *
  * | Name      | Width           | Direction | Description           |
  * |-----------|-----------------|-----------|-----------------------|
  * | clk       | 1               | Input     | Clock input           |
  * | cs_n      | 1               | Input     | Chip Select input     |
  * | wr_n      | 1               | Input     | Write Enable input    |
  * | rd_addr   | log_depth       | Input     | Read Address input    |
  * | wr_addr   | log_depth       | Input     | Write Address input   |
  * | data_in   | data_width      | Input     | Data input            |
  * | data_out  | data_width      | Output    | Data output           |
  *
  * @param data_width Width of the RAM data
  * @param depth Depth of the RAM
  */
class CW_ram_r_w_s_lat(val data_width: Int = 16, val depth: Int = 8)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth
      )
    )
    with HasBlackBoxPath {
  // Validations for parameters
  require(data_width >= 1, "data_width must be >= 1")
  require(data_width <= 256, "data_width must be <= 256")
  require(depth >= 2, "depth must be >= 2")
  require(depth <= 256, "depth must be <= 256")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val cs_n:     Bool  = Input(Bool())
    val wr_n:     Bool  = Input(Bool())
    val rd_addr:  UInt  = Input(UInt(log2Ceil(depth).W))
    val wr_addr:  UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:  UInt  = Input(UInt(data_width.W))
    val data_out: UInt  = Output(UInt(data_width.W))
  })
}
