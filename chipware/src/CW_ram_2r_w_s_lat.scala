import chisel3._
import chisel3.util.{log2Ceil, HasBlackBoxPath}
import chisel3.experimental._

/**
  * == CW_ram_2r_w_s_lat ==
  *
  * === Abstract ===
  *
  * CW_ram_2r_w_s_lat is a LATCH based synchronous, parameterized, dual port, static RAM.
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Range  | Default  | Description                              |
  * |--------------|--------------|----------|------------------------------------------|
  * | data_width   | 1 to 256     |          | Data width in bits.                      |
  * | depth        | 2 to 256     |          | Depth of the RAM.                        |
  *
  * === Ports ===
  *
  * | Name            | Width                 | Direction | Description                             |
  * |-----------------|-----------------------|-----------|-----------------------------------------|
  * | clk             | 1                     | Input     | Clock signal.                            |
  * | cs_n            | 1                     | Input     | Chip select signal.                      |
  * | wr_n            | 1                     | Input     | Write enable signal.                     |
  * | rd1_addr        | log2Ceil(depth)       | Input     | Read port 1 address.                     |
  * | rd2_addr        | log2Ceil(depth)       | Input     | Read port 2 address.                     |
  * | wr_addr         | log2Ceil(depth)       | Input     | Write address.                           |
  * | data_in         | data_width            | Input     | Input data.                              |
  * | data_rd1_out    | data_width            | Output    | Output data for read port 1.             |
  * | data_rd2_out    | data_width            | Output    | Output data for read port 2.             |
  *
  * @param data_width  Data width in bits.
  * @param depth       Depth of the RAM.
  */
class CW_ram_2r_w_s_lat(val data_width: Int = 16, val depth: Int = 8)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth
      )
    )
    with HasBlackBoxPath {
  require(data_width >= 1 && data_width <= 256, "data_width must be in the range [1, 256]")
  require(depth >= 2 && depth <= 256, "depth must be in the range [2, 256]")

  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val cs_n:         Bool  = Input(Bool())
    val wr_n:         Bool  = Input(Bool())
    val rd1_addr:     UInt  = Input(UInt(log2Ceil(depth).W))
    val rd2_addr:     UInt  = Input(UInt(log2Ceil(depth).W))
    val wr_addr:      UInt  = Input(UInt(log2Ceil(depth).W))
    val data_in:      UInt  = Input(UInt(data_width.W))
    val data_rd1_out: UInt  = Output(UInt(data_width.W))
    val data_rd2_out: UInt  = Output(UInt(data_width.W))
  })
}
