import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_sqrt_seq ==
  *
  * === Abstract ===
  *
  * Sequential Square Root
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width | >=6 | 6 | Word length of a |
  * | tc_mode | 0 or 1 | 0 | Two's complement control |
  * | num_cyc | >=3 and =< width | 3 | User-defined number of clock cycles to produce a valid result. The real number of clock cycles depends on input_mode, output_mode, early_start. |
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
  * | a | width bit(s) | Input | Radicand |
  * | complete | 1 bit | Output | Operation completed (=1) |
  * | root | (width +1)/2 bit(s) | Output | Square root |
  *
  * @param width Word length of a
  * @param tc_mode Two's complement control
  * @param num_cyc User-defined number of clock cycles to produce a valid result. The real number of clock cycles depends on input_mode, output_mode, early_start.
  * @param rst_mode Reset mode
  * @param input_mode Registered inputs
  * @param output_mode Registered outputs
  * @param early_start Computation start
  */
class CW_sqrt_seq(
  val width:       Int = 6,
  val tc_mode:     Int = 0,
  val num_cyc:     Int = 3,
  val rst_mode:    Int = 0,
  val input_mode:  Int = 1,
  val output_mode: Int = 1,
  val early_start: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "tc_mode" -> tc_mode,
        "num_cyc" -> num_cyc,
        "rst_mode" -> rst_mode,
        "input_mode" -> input_mode,
        "output_mode" -> output_mode,
        "early_start" -> early_start
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 6, s"width must be >= 6, but got $width")
  require(tc_mode >= 0 && tc_mode <= 1, s"tc_mode should be in range [0, 1], but got $tc_mode")
  require(
    num_cyc >= 3 && num_cyc <= (width + 1) / 2,
    s"num_cyc should be in range [3, (width + 1) / 2], but got $num_cyc"
  )
  require(rst_mode >= 0 && rst_mode <= 1, s"rst_mode should be in range [0, 1], but got $rst_mode")
  require(input_mode >= 0 && input_mode <= 1, s"input_mode should be in range [0, 1], but got $input_mode")
  require(output_mode >= 0 && output_mode <= 1, s"output_mode should be in range [0, 1], but got $output_mode")
  require(early_start >= 0 && early_start <= 1, s"early_start should be in range [0, 1], but got $early_start")
  require(
    !((input_mode == 0) && (early_start == 1)),
    s"Incorrect Parameter combination,input_mode=0 and early_start=1, but got $input_mode and $early_start"
  )
  protected val rootWidth: Int = (width + 1) / 2
  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val rst_n:    Bool  = Input(Bool())
    val hold:     Bool  = Input(Bool())
    val start:    Bool  = Input(Bool())
    val a:        UInt  = Input(UInt(width.W))
    val complete: Bool  = Output(Bool())
    val root:     UInt  = Output(UInt(rootWidth.W))
  })
}
