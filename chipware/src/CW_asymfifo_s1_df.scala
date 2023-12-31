import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_asymfifo_s1_df ==
  *
  * === Abstract ===
  *
  * Asymmetric I/O Synchronous (One Clock) FIFO - Dynamic Flags
  *
  * === Parameters ===
  *
  * | Parameter       | Legal Range | Default | Description |
  * |-----------------|-------------|---------|-------------|
  * | data_in_width   | 1 to 256    | 4       | Width of the data_in bus. data_in_width must be in an integer-multiple relationship with data_out_width. That is, either data_in_width = K × data_out_width, or data_out_width = K × data_in_width, where K is an integer. |
  * | data_out_width  | 1 to 256    | 16      | Width of the data_out bus. data_out_width must be in an integer-multiple relationship with data_in_width. That is, either data_in_width = K × data_out_width, or data_out_width = K × data_in_width, where K is an integer. |
  * | depth           | 2 to 256    | 10      | Number of memory elements used in the FIFO (addr_width = ceil[log2(depth)]) |
  * | err_mode        | 0 to 2      | 2       | Error mode |
  * | rst_mode        | 0 to 3      | 1       | Reset mode |
  * | byte_order      | 0 or 1      | 0       | Order of send/receive bytes or subword |
  *
  * === Ports ===
  *
  * | Name         | Width            | Direction | Description |
  * |--------------|------------------|-----------|-------------|
  * | clk          | 1 bit            | Input     | Input clock |
  * | rst_n        | 1 bit            | Input     | Reset input, active low (asynchronous if rst_mode = 0, synchronous if rst_mode = 1) |
  * | push_req_n   | 1 bit            | Input     | FIFO push request, active low |
  * | flush_n      | 1 bit            | Input     | Flushes the partial word into memory (fills in 0's)(for data_in_width < data_out_width only) |
  * | pop_req_n    | 1 bit            | Input     | FIFO pop request, active low |
  * | diag_n       | 1 bit            | Input     | Diagnostic control, active low (for err_mode = 0, NC for other err_mode values) |
  * | data_in      | data_in_width    | Input     | FIFO data to push |
  * | ae_level     | ceil(log2[depth])| Input     | Almost empty level (the number of words in the FIFO at or below which the almost_empty flag is active) |
  * | af_thresh    | ceil(log2[depth])| Input     | Almost full threshold(the number of words stored in the FIFO at or above which the almost_full flag is active) |
  * | empty        | 1 bit            | Output    | FIFO empty output, active high |
  * | almost_empty | 1 bit            | Output    | FIFO almost empty output, active high, asserted when FIFO level <= ae_level |
  * | half_full    | 1 bit            | Output    | FIFO half full output, active high |
  * | almost_full  | 1 bit            | Output    | FIFO almost full output, active high, asserted when FIFO level >= (af_thresh) |
  * | full         | 1 bit            | Output    | FIFO full output, active high |
  * | ram_full     | 1 bit            | Output    | RAM full output, active high |
  * | error        | 1 bit            | Output    | FIFO error output, active high |
  * | part_wd      | 1 bit            | Output    | Partial word, active high (for data_in_width < data_out_width only; otherwise, tied low) |
  * | data_out     | data_out_width   | Output    | FIFO data to pop |
  *
  * @param data_in_width  Width of the data_in bus
  * @param data_out_width Width of the data_out bus
  * @param depth          Number of memory elements used in the FIFO
  * @param err_mode       Error mode
  * @param rst_mode       Reset mode
  * @param byte_order     Order of send/receive bytes or subword
  */
class CW_asymfifo_s1_df(
  val data_in_width:  Int = 4,
  val data_out_width: Int = 16,
  val depth:          Int = 10,
  val err_mode:       Int = 2,
  val rst_mode:       Int = 1,
  val byte_order:     Int = 0)
    extends BlackBox(
      Map(
        "data_in_width" -> data_in_width,
        "data_out_width" -> data_out_width,
        "depth" -> depth,
        "err_mode" -> err_mode,
        "rst_mode" -> rst_mode,
        "byte_order" -> byte_order
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(data_in_width >= 1 && data_in_width <= 256, "data_in_width must be in range [1, 256]")
  require(data_out_width >= 1 && data_out_width <= 256, "data_out_width must be in range [1, 256]")
  require(depth >= 2 && depth <= 256, "depth must be in range [2, 256]")
  require(err_mode >= 0 && err_mode <= 2, "err_mode must be in range [0, 2]")
  require(rst_mode >= 0 && rst_mode <= 3, "rst_mode must be in range [0, 3]")
  require(byte_order == 0 || byte_order == 1, "byte_order must be 0 or 1")

  protected val addr_width: Int = log2Ceil(depth)

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val flush_n:      Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt(data_in_width.W))
    val ae_level:     UInt  = Input(UInt(addr_width.W))
    val af_thresh:    UInt  = Input(UInt(addr_width.W))
    val empty:        Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val ram_full:     Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
    val part_wd:      Bool  = Output(Bool())
    val data_out:     UInt  = Output(UInt(data_out_width.W))
  })
}
