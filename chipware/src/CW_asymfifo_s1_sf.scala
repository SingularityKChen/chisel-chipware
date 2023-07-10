import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_asymfifo_s1_sf ==
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | data_in_width  | 1 to 256  | 4  | Width of the data_in bus. Values for data_in_width must be integer-multiple relationship with data_out_width. That is,either data_in_width = K × data_out_width, or data_out_width= K ×  data_in_width, where K is an integer. |
  * | data_out_width  | 1 to 256  | 16  | Width of the data_out bus. data_out_width must be in an integer-multiple relationship with data_in_width. That is, either data_in_width = K × data_out_width, or data_out_width = K ×  data_in_width, where K is an integer. |
  * | depth  | 2 to 256  | 10  | Number of memory elements used in FIFO (used to size the address ports) Default: 4 |
  * | ae_level  | 1 to depth ­ 1  | 1  | Almost empty level (the number of words in the FIFO at or below which the almost_empty flag is active) Default: 1 |
  * | af_level  | 1 to depth ­ 1  | 9  | Almost full level (the number of empty memory locations in the FIFO at which the this flag is active. Default: 1 |
  * | err_mode  | 0 to 2  | 2  | Error mode Default: 1 0 = underflow/overflow and pointer latched checking, 1 = underflow/overflow latched checking, 2 = underflow/overflow unlatched checking |
  * | rst_mode  | 0 or 3  | 1  | Reset mode Default: 0 0 = asynchronous reset, 1 = synchronous reset |
  * | byte_order  | 0 or 1  | 0  | Order of bytes or subword within a word Default : 0 0 = first byte is in most significant bits position; 1 = first byte is in the least significant bits position). |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | clk  | 1 bit  | Input | Input clock |
  * | rst_n  | 1 bit  | Input | Reset input, active low asynchronous if rst_mode = 0, synchronous if rst_mode = 1 |
  * | push_req_n  | 1 bit  | Input | FIFO push request, active low |
  * | flush_n  | 1 bit  | Input | Flushes the partial word into memory (fills in 0's) (for data_in_width < data_out_width only) |
  * | pop_req_n  | 1 bit  | Input | FIFO pop request, active low |
  * | diag_n  | 1 bit  | Input | Diagnostic control |
  * | data_in  | data_in_width bits  | Input | FIFO data to push |
  * | empty  | 1 bit  | Output | FIFO empty output, active high |
  * | almost_empty  | 1 bit  | Output | FIFO almost empty output, asserted when FIFO level  <= ae_level, active high |
  * | half_full  | 1 bit  | Output | FIFO half full output, active high |
  * | almost_full  | 1 bit  | Output | FIFO almost full output, asserted when FIFO level >= (depth ­ af_level), active high |
  * | full  | 1 bit  | Output | FIFO full output, active high |
  * | ram_full  | 1 bit  | Output | RAM full output, active high |
  * | error  | 1 bit  | Output | FIFO error output, active high |
  * | part_wd  | 1 bit  | Output | Partial word, active high (for data_in_width < data_out_width only; otherwise, tied low) |
  * | data_out  | data_out_width bits  | Output | FIFO data to pop |
  *
  * @param data_in_width Width of the data_in bus. Values for data_in_width must be integer-multiple relationship with data_out_width. That is,either data_in_width = K × data_out_width, or data_out_width= K ×  data_in_width, where K is an integer.
  * @param data_out_width Width of the data_out bus. data_out_width must be in an integer-multiple relationship with data_in_width. That is, either data_in_width = K × data_out_width, or data_out_width = K ×  data_in_width, where K is an integer.
  * @param depth Number of memory elements used in FIFO (used to size the address ports) Default: 4
  * @param ae_level Almost empty level (the number of words in the FIFO at or below which the almost_empty flag is active) Default: 1
  * @param af_level Almost full level (the number of empty memory locations in the FIFO at which the this flag is active. Default: 1
  * @param err_mode Error mode Default: 1 0 = underflow/overflow and pointer latched checking, 1 = underflow/overflow latched checking, 2 = underflow/overflow unlatched checking
  * @param rst_mode Reset mode Default: 0 0 = asynchronous reset, 1 = synchronous reset
  * @param byte_order Order of bytes or subword within a word Default : 0 0 = first byte is in most significant bits position; 1 = first byte is in the least significant bits position).
  */
class CW_asymfifo_s1_sf(
  val data_in_width:  Int = 4,
  val data_out_width: Int = 16,
  val depth:          Int = 10,
  val ae_level:       Int = 1,
  val af_level:       Int = 9,
  val err_mode:       Int = 2,
  val rst_mode:       Int = 1,
  val byte_order:     Int = 0)
    extends BlackBox(
      Map(
        "data_in_width" -> data_in_width,
        "data_out_width" -> data_out_width,
        "depth" -> depth,
        "ae_level" -> ae_level,
        "af_level" -> af_level,
        "err_mode" -> err_mode,
        "rst_mode" -> rst_mode,
        "byte_order" -> byte_order
      )
    ) {
  require(data_in_width >= 1 && data_in_width <= 256, "data_in_width must be in range [1, 256]")
  require(data_out_width >= 1 && data_out_width <= 256, "data_out_width must be in range [1, 256]")
  require(depth >= 2 && depth <= 256, "depth must be in range [2, 256]")
  require(ae_level >= 1 && ae_level <= depth - 1, "ae_level must be in range [1, depth - 1]")
  require(af_level >= 1 && af_level <= depth - 1, "af_level must be in range [1, depth - 1]")
  require(err_mode >= 0 && err_mode <= 2, "err_mode must be in range [0, 2]")
  require(rst_mode == 0 || rst_mode == 3, "rst_mode must be either 0 or 3")
  require(byte_order == 0 || byte_order == 1, "byte_order must be either 0 or 1")

  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val flush_n:      Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt(data_in_width.W))
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
