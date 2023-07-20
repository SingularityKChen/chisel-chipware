// filename: CW_ram_2r_w_s_dff.scala
import chisel3._
import chisel3.util.{log2Ceil, HasBlackBoxPath}
import chisel3.experimental._

/**
  * == CW_ram_2r_w_s_dff ==
  *
  * === Abstract ===
  *
  * Synchronous Write Port, Asynchronous Dual Read Port RAM Flip-Flop Based.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range | Default | Description                                       |
  * |------------|-------------|---------|---------------------------------------------------|
  * | data_width | 1 to 256    | 16      | Width of the data input/output                    |
  * | depth      | 2 to 256    | 8       | Depth of the RAM                                  |
  * | rst_mode   | 0 to 1      | 0       | Reset mode (0: synchronous, 1: asynchronous)      |
  *
  * === Ports ===
  *
  * | Name          | Width                 | Direction | Description                                      |
  * |---------------|-----------------------|-----------|--------------------------------------------------|
  * | clk           | 1                     | Input     | Clock input                                      |
  * | rst_n         | 1                     | Input     | Asynchronous active-low reset input              |
  * | cs_n          | 1                     | Input     | Chip select input (active-low)                   |
  * | wr_n          | 1                     | Input     | Write enable input (active-low)                  |
  * | rd1_addr      | ceil(log2Ceil(depth)) | Input     | Address for the first read port                   |
  * | rd2_addr      | ceil(log2Ceil(depth)) | Input     | Address for the second read port                  |
  * | wr_addr       | ceil(log2Ceil(depth)) | Input     | Address for the write port                        |
  * | data_in       | data_width            | Input     | Data input                                       |
  * | data_rd1_out  | data_width            | Output    | Data output for the first read port               |
  * | data_rd2_out  | data_width            | Output    | Data output for the second read port              |
  *
  * @param data_width Width of the data input/output
  * @param depth      Depth of the RAM
  * @param rst_mode   Reset mode (0: synchronous, 1: asynchronous)
  */
class CW_ram_2r_w_s_dff(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "depth" -> depth,
        "rst_mode" -> rst_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(
    data_width >= 1 && data_width <= 256,
    s"ERROR - Bad parameter, data_width=$data_width, which is not in the legal range of 1 to 256."
  )
  require(
    depth >= 2 && depth <= 256,
    s"ERROR - Bad parameter, depth=$depth, which is not in the legal range of 2 to 256."
  )
  require(
    rst_mode >= 0 && rst_mode <= 1,
    s"ERROR - Bad parameter, rst_mode=$rst_mode, which is not in the legal range of 0 to 1."
  )

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
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
