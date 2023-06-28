// filename: CW_ram_rw_s_dff.scala
import chisel3._
import chisel3.experimental._
import chisel3.util._

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_ram_rw_s_dff ==
  *
  * === Abstract ===
  *
  * Synchronous Write Port, Asynchronous Read Port RAM Flip-Flop Based.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default | Description |
  * |------------|--------------|---------|-------------|
  * | data_width | 1 to 256     |         | Data width  |
  * | depth      | 2 to 256     |         | RAM depth   |
  * | rst_mode   | 0 to 1       |         | Reset mode  |
  *
  * === Ports ===
  *
  * | Name      | Width         | Direction | Description      |
  * |-----------|---------------|-----------|------------------|
  * | clk       | 1             | Input     | Clock            |
  * | rst_n     | 1             | Input     | Reset (active low) |
  * | cs_n      | 1             | Input     | Chip select (active low) |
  * | wr_n      | 1             | Input     | Write enable (active low) |
  * | rw_addr   | log_depth     | Input     | Read/write address |
  * | data_in   | data_width    | Input     | Data input       |
  * | data_out  | data_width    | Output    | Data output      |
  *
  * @param data_width Data width
  * @param depth RAM depth
  * @param rst_mode Reset mode
  */
class CW_ram_rw_s_dff(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth,
        "rst_mode" -> rst_mode
      )
    ) {
  // Parameter validations
  require(data_width >= 1 && data_width <= 256, s"data_width must be in the range of 1 to 256, got $data_width")
  require(depth >= 2 && depth <= 256, s"depth must be in the range of 2 to 256, got $depth")
  require(rst_mode >= 0 && rst_mode <= 1, s"rst_mode must be in the range of 0 to 1, got $rst_mode")

  // Port declarations
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val rst_n:    Bool  = Input(Bool())
    val cs_n:     Bool  = Input(Bool())
    val wr_n:     Bool  = Input(Bool())
    val rw_addr:  UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:  UInt  = Input(UInt(data_width.W))
    val data_out: UInt  = Output(UInt(data_width.W))
  })
}
