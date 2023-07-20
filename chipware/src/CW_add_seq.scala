import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath // To enable experimental features

/**
  * == CW_add_seq ==
  *
  * === Abstract ===
  *
  * CW_add_seq is a sequential adder designed for low area,
  * area-time trade-off, or high frequency (small cycle time) applications.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | a_width  | >=3  | 3 | Word length of a |
  * | b_width  | >=3  | 3 | Word length of b |
  * | num_cyc  | >=3 and <=(a_width or b_width) | 3 | User-defined number of clock cycles to produce a valid result. |
  * | rst_mode  | 0 or 1 | 0 | Reset mode |
  * | input_mode  | 0 or 1 | 1 | Registered inputs |
  * | output_mode  | 0 or 1 | 1 | Registered outputs |
  * | early_start  | 0 or 1 | 0 | Computation start |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | clk | 1 bit | Input | Clock |
  * | rst_n | 1 bit | Input | Reset, active low |
  * | hold | 1 bit | Input | Hold current operation (=1) |
  * | start | 1 bit | Input | Start operation (=1). A new operation is started again by making start=1 for one clock cycle. |
  * | a | a_width bit(s) | Input | Addend |
  * | b | b_width bit(s) | Input | Addend |
  * | complete | 1 bit | Output | Operation completed (=1) |
  * | sum | a_width bit(s) | Output | Sum a + b |
  *
  * @param a_width Word length of a
  * @param b_width Word length of b
  * @param num_cyc User-defined number of clock cycles to produce a valid result.
  * @param rst_mode Reset mode
  * @param input_mode Registered inputs
  * @param output_mode Registered outputs
  * @param early_start Computation start
  */
class CW_add_seq(
  val a_width:     Int = 3,
  val b_width:     Int = 3,
  val num_cyc:     Int = 3,
  val rst_mode:    Int = 0,
  val input_mode:  Int = 1,
  val output_mode: Int = 1,
  val early_start: Int = 0)
    extends BlackBox(
      Map(
        "a_width" -> a_width,
        "b_width" -> b_width,
        "num_cyc" -> num_cyc,
        "rst_mode" -> rst_mode,
        "input_mode" -> input_mode,
        "output_mode" -> output_mode,
        "early_start" -> early_start
      )
    )
    with HasBlackBoxPath {
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val rst_n:    Bool  = Input(Bool())
    val hold:     Bool  = Input(Bool())
    val start:    Bool  = Input(Bool())
    val a:        UInt  = Input(UInt(a_width.W))
    val b:        UInt  = Input(UInt(b_width.W))
    val complete: Bool  = Output(Bool())
    val sum:      UInt  = Output(UInt(a_width.W))
  })
}
