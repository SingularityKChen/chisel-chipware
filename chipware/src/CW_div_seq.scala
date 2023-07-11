import chisel3._
import chisel3.experimental._

/**
  * == CW_div_seq ==
  *
  * === Abstract ===
  *
  * Sequential Divider
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | a_width | >= 3 | 3 | Word length of a |
  * | b_width | >= 3 and =< a_width | 3 | Word length of b |
  * | tc_mode | 0 or 1 | 0 | Two's complement control |
  * | num_cyc | >= 3 and =< a_width | 3 | User-defined number of clock cycles to produce a valid result. The real number of clock cycles depends on various parameters. |
  * | rst_mode | 0 or 1 | 0 | Reset mode |
  * | input_mode | 0 or 1 | 1 | Registered inputs |
  * | output_mode | 0 or 1 | 1 | Registered outputs |
  * | early_start | 0 or 1 | 0 | Computation start |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | clk | 1 bit | Input | Clock |
  * | rst_n | 1 bit | Input | Reset, active low |
  * | hold | 1 bit | Input | Hold current operation (=1) |
  * | start | 1 bit | Input | Start operation (=1). A new operation is started by setting start=1 for one clock cycle. |
  * | a | a_width bit(s) | Input | Dividend |
  * | b | b_width bit(s) | Input | Divisor |
  * | complete | 1 bit | Output | Operation completed (=1) |
  * | divide_by_0 | 1 bit | Output | Indicates if b equals 0 |
  * | quotient | a_width bit(s) | Output | Quotient |
  * | remainder | b_width bit(s) | Output | Remainder |
  *
  * @param a_width Word length of a
  * @param b_width Word length of b
  * @param tc_mode Two's complement control
  * @param num_cyc User-defined number of clock cycles to produce a valid result. The real number of clock cycles depends on various parameters.
  * @param rst_mode Reset mode
  * @param input_mode Registered inputs
  * @param output_mode Registered outputs
  * @param early_start Computation start
  */
class CW_div_seq(
  val a_width:     Int = 3,
  val b_width:     Int = 3,
  val tc_mode:     Int = 0,
  val num_cyc:     Int = 3,
  val rst_mode:    Int = 0,
  val input_mode:  Int = 1,
  val output_mode: Int = 1,
  val early_start: Int = 0)
    extends BlackBox(
      Map(
        "a_width" -> a_width,
        "b_width" -> b_width,
        "tc_mode" -> tc_mode,
        "num_cyc" -> num_cyc,
        "rst_mode" -> rst_mode,
        "input_mode" -> input_mode,
        "output_mode" -> output_mode,
        "early_start" -> early_start
      )
    ) {
  // Validation of all parameters
  require(a_width >= 3, s"a_width must be >= 3, got $a_width")
  require(b_width >= 3 && b_width <= a_width, s"b_width should in range [3, $a_width], got $b_width")
  require(tc_mode >= 0 && tc_mode <= 1, s"tc_mode should in range [0, 1], got $tc_mode")
  require(num_cyc >= 3 && num_cyc <= a_width, s"num_cyc should in range [3, $a_width], got $num_cyc")
  require(rst_mode >= 0 && rst_mode <= 1, s"rst_mode should in range [0, 1], got $rst_mode")
  require(input_mode >= 0 && input_mode <= 1, s"input_mode should in range [0, 1], got $input_mode")
  require(output_mode >= 0 && output_mode <= 1, s"output_mode should in range [0, 1], got $output_mode")
  require(early_start >= 0 && early_start <= 1, s"early_start should in range [0, 1], got $early_start")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val hold:        Bool  = Input(Bool())
    val start:       Bool  = Input(Bool())
    val a:           UInt  = Input(UInt(a_width.W))
    val b:           UInt  = Input(UInt(b_width.W))
    val complete:    Bool  = Output(Bool())
    val divide_by_0: Bool  = Output(Bool())
    val quotient:    UInt  = Output(UInt(a_width.W))
    val remainder:   UInt  = Output(UInt(b_width.W))
  })
}
