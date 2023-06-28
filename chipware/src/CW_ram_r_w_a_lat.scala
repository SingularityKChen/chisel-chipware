import chisel3._
import chisel3.experimental._
import chisel3.util.log2Ceil

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_ram_r_w_a_lat ==
  *
  * === Abstract ===
  *
  * Synchronous Write Port, Asynchronous Read Port RAM Latched Based.
  *
  * === Parameters ===
  *
  * | Parameter     | Legal Range  | Default      | Description        |
  * |---------------|--------------|--------------|--------------------|
  * | data_width    | > 0          | -            | Data width         |
  * | depth         | > 1          | -            | Depth              |
  * | rst_mode      | 0 to 1       | -            | Reset mode         |
  *
  * === Ports ===
  *
  * | Name      | Width                  | Direction | Description                   |
  * |-----------|------------------------|-----------|-------------------------------|
  * | rst_n     | 1                      | Input     | Reset (active low)            |
  * | cs_n      | 1                      | Input     | Chip Select (active low)      |
  * | wr_n      | 1                      | Input     | Write Enable (active low)     |
  * | rd_addr   | log2Ceil(depth)        | Input     | Read Address                  |
  * | wr_addr   | log2Ceil(depth)        | Input     | Write Address                 |
  * | data_in   | data_width             | Input     | Data Input                    |
  * | data_out  | data_width             | Output    | Data Output                   |
  *
  * @param data_width Data width
  * @param depth Depth
  * @param rst_mode Reset mode
  */
class CW_ram_r_w_a_lat(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth,
        "rst_mode" -> rst_mode
      )
    ) {
  // Validation of all parameters
  require(data_width > 0, "data_width must be > 0")
  require(depth > 1, "depth must be > 1")
  require(rst_mode >= 0 && rst_mode <= 1, "rst_mode should be in the legal range of 0 to 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_n:    Bool = Input(Bool())
    val cs_n:     Bool = Input(Bool())
    val wr_n:     Bool = Input(Bool())
    val rd_addr:  UInt = Input(UInt(log2Ceil(depth).W))
    val wr_addr:  UInt = Input(UInt(log2Ceil(depth).W))
    val data_in:  UInt = Input(UInt(data_width.W))
    val data_out: UInt = Output(UInt(data_width.W))
  })
}
