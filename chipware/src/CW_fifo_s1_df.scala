// Provide the necessary imports
import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_fifo_s1_df ==
  *
  * === Abstract ===
  *
  * Synchronous (Single Clock) FIFO with Dynamic Flags
  *
  * === Parameters ===
  *
  * | Parameter | Legal Range | Default | Description  |
  * |-----------|-------------|---------|--------------|
  * | width     | 1 to 256    | 8       | Width of data_in and data_out buses |
  * | depth     | 2 to 256    | 4       | Number of memory elements used in FIFO |
  * | err_mode  | 0 to 2      | 0       | Error mode |
  * | rst_mode  | 0 to 3      | 0       | Reset mode |
  *
  * === Ports ===
  *
  * | Name         | Width                  | Direction | Description                       |
  * |--------------|------------------------|-----------|-----------------------------------|
  * | clk          | 1 bit                  | Input     | Input clock                       |
  * | rst_n        | 1 bit                  | Input     | Reset input, active low           |
  * | push_req_n   | 1 bit                  | Input     | FIFO push request, active low     |
  * | pop_req_n    | 1 bit                  | Input     | FIFO pop request, active low      |
  * | diag_n       | 1 bit                  | Input     | Diagnostic control, active low    |
  * | ae_level     | ceil(log2[depth]) bits | Input     | Almost empty level                |
  * | af_thresh    | ceil(log2[depth]) bits | Input     | Almost full threshold             |
  * | data_in      | width bits             | Input     | FIFO data to push                 |
  * | empty        | 1 bit                  | Output    | FIFO empty output                 |
  * | almost_empty | 1 bit                  | Output    | FIFO almost empty output          |
  * | half_full    | 1 bit                  | Output    | FIFO half full output             |
  * | almost_full  | 1 bit                  | Output    | FIFO almost full output           |
  * | full         | 1 bit                  | Output    | FIFO full output                  |
  * | error        | 1 bit                  | Output    | FIFO error output                 |
  * | data_out     | width bits             | Output    | FIFO data to pop                  |
  *
  * @param width    Width of data_in and data_out buses
  * @param depth    Number of memory elements used in FIFO
  * @param err_mode Error mode
  * @param rst_mode Reset mode
  */
class CW_fifo_s1_df(val width: Int = 8, val depth: Int = 4, val err_mode: Int = 0, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "depth" -> depth,
        "err_mode" -> err_mode,
        "rst_mode" -> rst_mode
      )
    ) {
  // Validation of all parameters
  require(width >= 1 && width <= 256, "width must be in the range [1, 256]")
  require(depth >= 2 && depth <= 256, "depth must be in the range [2, 256]")
  require(err_mode >= 0 && err_mode <= 2, "err_mode must be in the range [0, 2]")
  require(rst_mode >= 0 && rst_mode <= 3, "rst_mode must be in the range [0, 3]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val ae_level:     UInt  = Input(UInt(log2Ceil(depth).W))
    val af_thresh:    UInt  = Input(UInt(log2Ceil(depth).W))
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
