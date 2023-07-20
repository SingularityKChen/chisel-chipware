// filename: CW_asymdata_outbuf.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_asymdata_outbuf ==
  *
  * === Abstract ===
  *
  * asymemetric data output buffer
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | in_width      | 1 to 2<sup>9</sup>     | 16           | width of data_out |
  * | out_width     | 1 to 2<sup>9</sup>     | 8            | width of data_in |
  * | err_mode      | -            | 0            | This parameter is not used. It is a place holder. |
  * | byte_order    | 0 or 1       | 0            | 1st sub-word read/written is msb-subword or lsb-subword |
  *
  * === Ports ===
  *
  * | Name      | Width       | Direction | Description  |
  * |-----------|-------------|-----------|------------------------|
  * | clk_pop   | 1 bit       | Input     | functional clock |
  * | rst_pop_n | 1 bit       | Input     | asynchronous reset |
  * | init_pop_n| 1 bit       | Input     | synchronous reset |
  * | pop_req_n | 1 bit       | Input     | user pop request (active low) |
  * | data_in   | in_width    | Input     | input full-word |
  * | fifo_empty| 1 bit       | Input     | full status flag from FIFO controller |
  * | pop_wd_n  | 1 bit       | Output    | asymdata_outbuf initiated pop request (active low) |
  * | data_out  | out_width   | Output    | output partial-word |
  * | part_wd   | 1 bit       | Output    | partial-word poped flag |
  * | pop_error | 1 bit       | Output    | FIFO overflow |
  *
  * @param in_width   width of data_out
  * @param out_width  width of data_in
  * @param err_mode   This parameter is not used. It is a place holder.
  * @param byte_order 1st sub-word read/written is msb-subword or lsb-subword
  */
class CW_asymdata_outbuf(val in_width: Int = 16, val out_width: Int = 8, val err_mode: Int = 0, val byte_order: Int = 0)
    extends BlackBox(
      Map(
        "in_width" -> in_width,
        "out_width" -> out_width,
        "err_mode" -> err_mode,
        "byte_order" -> byte_order
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(in_width >= 1 && in_width <= math.pow(2, 9), "in_width must be in range [1, 2^9]")
  require(out_width >= 1 && out_width <= math.pow(2, 9), "out_width must be in range [1, 2^9]")
  require(byte_order == 0 || byte_order == 1, "byte_order must be 0 or 1")
  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk_pop:    Clock = Input(Clock())
    val rst_pop_n:  Bool  = Input(Bool())
    val init_pop_n: Bool  = Input(Bool())
    val pop_req_n:  Bool  = Input(Bool())
    val data_in:    UInt  = Input(UInt(in_width.W))
    val fifo_empty: Bool  = Input(Bool())
    val pop_wd_n:   Bool  = Output(Bool())
    val data_out:   UInt  = Output(UInt(out_width.W))
    val part_wd:    Bool  = Output(Bool())
    val pop_error:  Bool  = Output(Bool())
  })
}
