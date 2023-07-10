// filename: CW_asymdata_inbuf.scala
import chisel3._
import chisel3.experimental._

/**
  * == CW_asymdata_inbuf ==
  *
  * === Abstract ===
  *
  * asymemetric data input buffer
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | in_width      | 1 to 2<sup>9</sup>     | 8            | Width of data_in |
  * | out_width     | 1 to 2<sup>9</sup>     | 16           | Width of data_out |
  * | err_mode      | -            | 0            | This parameter is not used. It is a place holder. |
  * | byte_order    | 0 or 1       | 0            | 1st sub-word read/written is msb-subword or lsb-subword |
  * | flush_value   | 0 or 1       | 0            | unassigned sub-words flushed with zeros or ones |
  *
  * === Ports ===
  *
  * | Name        | Width       | Direction | Description  |
  * |-------------|-------------|-----------|--------------|
  * | clk_push    | 1 bit       | Input     | functional clock |
  * | rst_push_n  | 1 bit       | Input     | asynchronous reset |
  * | init_push_n | 1 bit       | Input     | synchronous reset |
  * | push_req_n  | 1 bit       | Input     | push requst (active low) |
  * | data_in     | in_width    | Input     | input partial-word |
  * | flush_n     | 1 bit       | Input     | flush request (active low) |
  * | fifo_full   | 1 bit       | Input     | full status flag from FIFO controller |
  * | data_out    | out_width   | Output    | output full-word |
  * | inbuf_full  | 1 bit       | Output    | all partial-word registers contain partial-words |
  * | push_wd_n   | 1 bit       | Output    | push_wd_n |
  * | part_wd     | 1 bit       | Output    | partial-word pushed flag |
  * | push_error  | 1 bit       | Output    | FIFO overflow |
  *
  * @param in_width    Width of data_in
  * @param out_width   Width of data_out
  * @param err_mode    This parameter is not used. It is a place holder.
  * @param byte_order  1st sub-word read/written is msb-subword or lsb-subword
  * @param flush_value unassigned sub-words flushed with zeros or ones
  */
class CW_asymdata_inbuf(
  val in_width:    Int = 8,
  val out_width:   Int = 16,
  val err_mode:    Int = 0,
  val byte_order:  Int = 0,
  val flush_value: Int = 0)
    extends BlackBox(
      Map(
        "in_width" -> in_width,
        "out_width" -> out_width,
        "err_mode" -> err_mode,
        "byte_order" -> byte_order,
        "flush_value" -> flush_value
      )
    ) {
  // Validation of all parameters
  require(in_width >= 1 && in_width <= math.pow(2, 9), "in_width must be in range [1, 2^9]")
  require(out_width >= 1 && out_width <= math.pow(2, 9), "out_width must be in range [1, 2^9]")
  require(byte_order == 0 || byte_order == 1, "byte_order must be 0 or 1")
  require(flush_value == 0 || flush_value == 1, "flush_value must be 0 or 1")
  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk_push:    Clock = Input(Clock())
    val rst_push_n:  Bool  = Input(Bool())
    val init_push_n: Bool  = Input(Bool())
    val push_req_n:  Bool  = Input(Bool())
    val data_in:     UInt  = Input(UInt(in_width.W))
    val flush_n:     Bool  = Input(Bool())
    val fifo_full:   Bool  = Input(Bool())
    val data_out:    UInt  = Output(UInt(out_width.W))
    val inbuf_full:  Bool  = Output(Bool())
    val push_wd_n:   Bool  = Output(Bool())
    val part_wd:     Bool  = Output(Bool())
    val push_error:  Bool  = Output(Bool())
  })
}
