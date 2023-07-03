// filename: CW_fifoctl_s1_df.scala
import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_fifoctl_s1_df ==
  *
  * === Abstract ===
  *
  * Synchronous (Single Clock) FIFO Controller with Dynamic Flags
  *
  * === Parameters ===
  *
  * | Parameter | Legal Range | Default | Description                                                     |
  * |-----------|-------------|---------|-----------------------------------------------------------------|
  * | depth     | 2 to 224    | 4       | Number of memory elements used in FIFO (used to size the address ports) |
  * | err_mode  | 0 to 2      | 0       | Error mode                                                      |
  * | rst_mode  | 0 or 1      | 0       | Reset mode                                                      |
  *
  * === Ports ===
  *
  * | Name           | Width             | Direction | Description                           |
  * |----------------|-------------------|-----------|---------------------------------------|
  * | clk            | 1 bit             | Input     | Input clock                           |
  * | rst_n          | 1 bit             | Input     | Reset input, active low               |
  * | push_req_n     | 1 bit             | Input     | FIFO push request, active low         |
  * | pop_req_n      | 1 bit             | Input     | FIFO pop request, active low          |
  * | diag_n         | 1 bit             | Input     | Diagnostic control                    |
  * | ae_level       | ceil(log2[depth]) | Input     | Almost empty level                    |
  * | af_thresh      | ceil(log2[depth]) | Input     | Almost full threshold                 |
  * | we_n           | 1 bit             | Output    | Write enable output for write port of RAM, active low |
  * | empty          | 1 bit             | Output    | FIFO empty output, active high        |
  * | almost_empty   | 1 bit             | Output    | FIFO almost empty output, active high |
  * | half_full      | 1 bit             | Output    | FIFO half full output, active high    |
  * | almost_full    | 1 bit             | Output    | FIFO almost full output, active high  |
  * | full           | 1 bit             | Output    | FIFO full output, active high         |
  * | error          | 1 bit             | Output    | FIFO error output, active high        |
  * | wr_addr        | ceil(log2[depth]) | Output    | Address output to write port of RAM   |
  * | rd_addr        | ceil(log2[depth]) | Output    | Address output to read port of RAM    |
  *
  * @param depth    Number of memory elements used in FIFO (used to size the address ports)
  * @param err_mode Error mode
  * @param rst_mode Reset mode
  */
class CW_fifoctl_s1_df(val depth: Int = 4, val err_mode: Int = 0, val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "depth" -> depth,
        "err_mode" -> err_mode,
        "rst_mode" -> rst_mode
      )
    ) {
  require(depth >= 2 && depth <= 224, "depth must be between 2 and 224")
  require(err_mode >= 0 && err_mode <= 2, "err_mode must be between 0 and 2")
  require(rst_mode == 0 || rst_mode == 1, "rst_mode must be either 0 or 1")

  protected val log_depth:         Int = log2Ceil(depth)
  protected val log_depth_antilog: Int = 1 << log_depth
  protected val offset:            Int = log_depth_antilog - depth
  protected val zero:              Int = 0
  protected val minus_one:         Int = (1 << (log_depth + 1)) - 1
  protected val plus_one:          Int = (1 << log_depth)

  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val ae_level:     UInt  = Input(UInt(log_depth.W))
    val af_thresh:    UInt  = Input(UInt(log_depth.W))
    val empty:        Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val rd_addr:      UInt  = Output(UInt(log_depth.W))
    val wr_addr:      UInt  = Output(UInt(log_depth.W))
    val we_n:         Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
  })
}
