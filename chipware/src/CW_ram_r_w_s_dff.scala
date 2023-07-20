// Filename: CW_ram_r_w_s_dff.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.{log2Ceil, HasBlackBoxPath}

/**
  * == CW_ram_r_w_s_dff ==
  *
  * === Abstract ===
  *
  * Synchronous Write Port, Asynchronous Read Port RAM Flip-Flop Based.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range | Default | Description                                          |
  * |------------|-------------|---------|------------------------------------------------------|
  * | data_width | [1, 2048]  | -       | Width of data                                       |
  * | depth      | [2, 1024]  | -       | Depth of RAM                                        |
  * | rst_mode   | [0, 1]     | -       | Reset mode (0: active-high, 1: active-low)           |
  *
  * === Ports ===
  *
  * | Name      | Width          | Direction | Description            |
  * |-----------|----------------|-----------|------------------------|
  * | clk       | 1              | Input     | Clock                  |
  * | rst_n     | 1              | Input     | Active-high reset       |
  * | cs_n      | 1              | Input     | Active-low chip select  |
  * | wr_n      | 1              | Input     | Active-low write enable |
  * | rd_addr   | log2Ceil(depth)| Input     | Read address           |
  * | wr_addr   | log2Ceil(depth)| Input     | Write address          |
  * | data_in   | data_width     | Input     | Input data             |
  * | data_out  | data_width     | Output    | Output data            |
  *
  * @param data_width Width of data
  * @param depth      Depth of RAM
  * @param rst_mode   Reset mode (0: active-high, 1: active-low)
  */
class CW_ram_r_w_s_dff(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth,
        "rst_mode" -> rst_mode
      )
    )
    with HasBlackBoxPath {
  require(data_width >= 1 && data_width <= 2048, s"data_width must be in the range [1, 2048], but got $data_width")
  require(depth >= 2 && depth <= 1024, s"depth must be in the range [2, 1024], but got $depth")
  require(rst_mode >= 0 && rst_mode <= 1, s"rst_mode must be in the range [0, 1], but got $rst_mode")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val rst_n:    Bool  = Input(Bool())
    val cs_n:     Bool  = Input(Bool())
    val wr_n:     Bool  = Input(Bool())
    val rd_addr:  UInt  = Input(UInt(log2Ceil(depth).W))
    val wr_addr:  UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:  UInt  = Input(UInt(data_width.W))
    val data_out: UInt  = Output(UInt(data_width.W))
  })
}
