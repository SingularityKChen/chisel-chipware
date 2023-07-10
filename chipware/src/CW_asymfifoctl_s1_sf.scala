// filename: CW_asymfifoctl_s1_sf.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.log2Ceil

/**
  * == CW_asymfifoctl_s1_sf ==
  *
  * === Abstract ===
  *
  * Asym. I/O Synch. (Single Clock) FIFO Controller - Static Flags
  *
  * === Parameters ===
  *
  * | Parameter      | Legal Range        | Default | Description                                                                 |
  * |----------------|--------------------|---------|-----------------------------------------------------------------------------|
  * | data_in_width  | 1 to 256           | 4       | Width of the data_in bus. Values for data_in_width must be in an integer-multiple relationship with data_out_width. That is, either data_in_width = K × data_out_width, or data_out_width = K × data_in_width, where K is an integer. |
  * | data_out_width | 1 to 256           | 16      | Width of the data_out bus. data_out_width must be in an integer-multiple relationship with data_in_width. That is, either data_in_width = K × data_out_width, or data_out_width = K × data_in_width, where K is an integer. |
  * | depth          | 2 to 224           | 10      | Number of memory elements used in the FIFO (addr_width = ceil[log2(depth)]) |
  * | ae_level       | 1 to depth-1       | 1       | Almost empty level (the number of words in the FIFO at or below which the almost_empty flag is active) |
  * | af_level       | 1 to depth-1       | 9       | Almost full level (the number of empty memory locations in the FIFO at which the almost_full flag is active. |
  * | err_mode       | 0 to 2             | 1       | Error mode Default: 1 0 = underflow/overflow with pointer latched checking, 1 = underflow/overflow latched checking, 2 = underflow/overflow unlatched checking). |
  * | rst_mode       | 0 or 1             | 1       | Reset mode Default: 1 0 = asynchronous reset, 1 = synchronous reset). |
  * | byte_order     | 0 or 1             | 0       | Order of bytes or subword [subword < 8 bits > subword] within a word 0 = first byte is in most significant bits position; 1 = first byte is in the least significant bits position). |
  *
  * === Ports ===
  *
  * | Name         | Width              | Direction | Description                                                                 |
  * |--------------|--------------------|-----------|-----------------------------------------------------------------------------|
  * | clk          | 1 bit             | Input     | Input clock                                                                 |
  * | rst_n        | 1 bit             | Input     | Reset input, active low asynchronous if rst_mode = 0, synchronous if rst_mode = 1) |
  * | push_req_n   | 1 bit             | Input     | FIFO push request, active low                                               |
  * | flush_n      | 1 bit             | Input     | Flushes the partial word into memory (fills in 0's) (for data_in_width < data_out_width only) |
  * | pop_req_n    | 1 bit             | Input     | FIFO pop request, active low                                                |
  * | diag_n       | 1 bit             | Input     | Diagnostic control, active low (for err_mode = 0,NC for other err_mode values) |
  * | data_in      | data_in_width bit(s) | Input     | FIFO data to push                                                            |
  * | rd_data      | max(data_in_width, data_out_width) bit(s) | Input | RAM data input to FIFO controller                                           |
  * | we_n         | 1 bit             | Output    | Write enable output for write port of RAM, active low                        |
  * | empty        | 1 bit             | Output    | FIFO empty output, active high                                               |
  * | almost_empty | 1 bit             | Output    | FIFO almost empty output, active high, asserted when FIFO level =< ae_level |
  * | half_full    | 1 bit             | Output    | FIFO half full output, active high                                           |
  * | almost_full  | 1 bit             | Output    | FIFO almost full output, active high, asserted when FIFO level >= (depth - af_level) |
  * | full         | 1 bit             | Output    | FIFO full output, active high                                                |
  * | ram_full     | 1 bit             | Output    | RAM full output, active high                                                 |
  * | error        | 1 bit             | Output    | FIFO error output, active high                                               |
  * | part_wd      | 1 bit             | Output    | Partial word, active high (for data_in_width < data_out_width only; otherwise, tied low) |
  * | wr_data      | max(data_in_width, data_out_width) bit(s) | Output | FIFO controller output data to RAM                                          |
  * | wr_addr      | ceil(log2[depth]) bit(s) | Output | Address output to write port of RAM                                          |
  * | rd_addr      | ceil(log2[depth]) bit(s) | Output | Address output to read port of RAM                                           |
  * | data_out     | data_out_width bit(s) | Output    | FIFO data to pop                                                             |
  *
  * @param data_in_width  Width of the data_in bus.
  * @param data_out_width Width of the data_out bus.
  * @param depth          Number of memory elements used in the FIFO.
  * @param ae_level       Almost empty level.
  * @param af_level       Almost full level.
  * @param err_mode       Error mode.
  * @param rst_mode       Reset mode.
  * @param byte_order     Order of bytes or subword within a word.
  */
class CW_asymfifoctl_s1_sf(
  val data_in_width:  Int = 4,
  val data_out_width: Int = 16,
  val depth:          Int = 10,
  val ae_level:       Int = 1,
  val af_level:       Int = 9,
  val err_mode:       Int = 1,
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
  require(
    data_in_width >= 1 && data_in_width <= 256,
    s"data_in_width must be in range [1, 256], but got $data_in_width"
  )
  require(
    data_out_width >= 1 && data_out_width <= 256,
    s"data_out_width must be in range [1, 256], but got $data_out_width"
  )
  require(depth >= 2 && depth <= 224, s"depth must be in range [2, 224], but got $depth")
  require(ae_level >= 1 && ae_level <= depth - 1, s"ae_level must be in range [1, depth-1], but got $ae_level")
  require(af_level >= 1 && af_level <= depth - 1, s"af_level must be in range [1, depth-1], but got $af_level")
  require(err_mode >= 0 && err_mode <= 2, s"err_mode must be in range [0, 2], but got $err_mode")
  require(rst_mode >= 0 && rst_mode <= 1, s"rst_mode must be in range [0, 1], but got $rst_mode")
  require(byte_order >= 0 && byte_order <= 1, s"byte_order must be in range [0, 1], but got $byte_order")

  protected val log_depth:      Int = log2Ceil(depth)
  protected val max_data_width: Int = if (data_in_width > data_out_width) data_in_width else data_out_width

  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val flush_n:      Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val data_in:      UInt  = Input(UInt(data_in_width.W))
    val rd_data:      UInt  = Input(UInt(max_data_width.W))
    val we_n:         Bool  = Output(Bool())
    val empty:        Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val ram_full:     Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
    val part_wd:      Bool  = Output(Bool())
    val wr_data:      UInt  = Output(UInt(max_data_width.W))
    val wr_addr:      UInt  = Output(UInt(log_depth.W))
    val rd_addr:      UInt  = Output(UInt(log_depth.W))
    val data_out:     UInt  = Output(UInt(data_out_width.W))
  })
}
