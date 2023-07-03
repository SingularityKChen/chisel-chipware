import chisel3._
import chisel3.experimental._

/**
  * == CW_fifo_s1_sf ==
  *
  * === Abstract ===
  *
  * Synchronous (Single Clock) FIFO with Static Flags
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range | Default | Description                                                  |
  * |------------|-------------|---------|--------------------------------------------------------------|
  * | width      | 1 to 256    | 8       | Width of the data_in and data_out buses                      |
  * | depth      | 2 to 256    | 4       | Number of memory elements used in FIFO                       |
  * | ae_level   | 1 to depth - 1  | 1       | Almost empty level (the number of words in the FIFO at or     |
  * |            |             |         | below which the almost_empty flag is active)                  |
  * | af_level   | 1 to depth - 1 | 1       | Almost full level (the number of empty memory locations       |
  * |            |             |         | in the FIFO at which the almost_full flag is active)          |
  * | err_mode   | 0 to 2      | 0       | Error mode                                                   |
  * |            |             |         | 0 = underflow/overflow and pointer latched checking,         |
  * |            |             |         | 1 = underflow/overflow latched checking,                     |
  * |            |             |         | 2 = underflow/overflow unlatched checking                    |
  * | rst_mode   | 0 to 3      | 0       | Reset mode                                                   |
  * |            |             |         | 0 = asynchronous reset including memory,                     |
  * |            |             |         | 1 = synchronous reset including memory,                      |
  * |            |             |         | 2 = asynchronous reset excluding memory,                     |
  * |            |             |         | 3 = synchronous reset excluding memory                       |
  *
  * === Ports ===
  *
  * | Name         | Width           | Direction | Description                |
  * |--------------|-----------------|-----------|----------------------------|
  * | clk          | 1 bit           | Input     | Input clock                |
  * | rst_n        | 1 bit           | Input     | Reset input, active low    |
  * | push_req_n   | 1 bit           | Input     | FIFO push request, active low |
  * | pop_req_n    | 1 bit           | Input     | FIFO pop request, active low |
  * | diag_n       | 1 bit           | Input     | Diagnostic control, active low |
  * | data_in      | width bit(s)    | Input     | FIFO data to push          |
  * | empty        | 1 bit           | Output    | FIFO empty output, active high |
  * | almost_empty | 1 bit           | Output    | FIFO almost empty output, active high |
  * | half_full    | 1 bit           | Output    | FIFO half full output, active high |
  * | almost_full  | 1 bit           | Output    | FIFO almost full output, active high |
  * | full         | 1 bit           | Output    | FIFO full output, active high |
  * | error        | 1 bit           | Output    | FIFO error output, active high |
  * | data_out     | width bit(s)    | Output    | FIFO data to pop           |
  *
  * @param width    Width of the data_in and data_out buses
  * @param depth    Number of memory elements used in FIFO
  * @param ae_level Almost empty level (the number of words in the FIFO at or below which the almost_empty flag is active)
  * @param af_level Almost full level (the number of empty memory locations in the FIFO at which the almost_full flag is active)
  * @param err_mode Error mode
  * @param rst_mode Reset mode
  */
class CW_fifo_s1_sf(
  val width:    Int = 8,
  val depth:    Int = 4,
  val ae_level: Int = 1,
  val af_level: Int = 1,
  val err_mode: Int = 0,
  val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "depth" -> depth,
        "ae_level" -> ae_level,
        "af_level" -> af_level,
        "err_mode" -> err_mode,
        "rst_mode" -> rst_mode
      )
    ) {
  require(width >= 1 && width <= 256, "width must be in the range [1, 256]")
  require(depth >= 2 && depth <= 256, "depth must be in the range [2, 256]")
  require(ae_level >= 1 && ae_level <= depth - 1, "ae_level must be in the range [1, depth - 1]")
  require(af_level >= 1 && af_level <= depth - 1, "af_level must be in the range [1, depth - 1]")
  require(err_mode >= 0 && err_mode <= 2, "err_mode must be in the range [0, 2]")
  require(rst_mode >= 0 && rst_mode <= 3, "rst_mode must be in the range [0, 3]")

  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt(width.W))
    val empty:        Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
    val data_out:     UInt  = Output(UInt(width.W))
  })
}
