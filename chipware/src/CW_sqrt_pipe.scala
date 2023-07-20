import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_sqrt_pipe ==
  *
  * === Abstract ===
  *
  * CW_sqrt_pipe is an n stage stallable pipelined Square Root
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width | >= 2 | 2 | Word length of a |
  * | tc_mode | 0 or 1 | 0 | Two's complement control: 0 for unsigned, 1 for two's complement |
  * | num_stages | >= 2 | 2 | Number of pipelined stages |
  * | stall_mode | 0 or 1 | 1 | Stall mode: 0 for non-stallable, 1 for stallable |
  * | rst_mode | 0 to 2 | 1 | Reset mode: 0 for no reset, 1 for asynchronous reset, 2 for synchronous reset |
  * | op_iso_mode | 0 to 4 | 0 | Operation isolation mode |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | clk | 1 | Input | Clock |
  * | rst_n | 1 | Input | Reset, active low |
  * | en | 1 | Input | Load enable, active low |
  * | a | width | Input | Radicand |
  * | root | (width+1)/2 | Output | Square root of a |
  *
  * @param width Word length of a
  * @param tc_mode Two's complement control: 0 for unsigned, 1 for two's complement
  * @param num_stages Number of pipelined stages
  * @param stall_mode Stall mode: 0 for non-stallable, 1 for stallable
  * @param rst_mode Reset mode: 0 for no reset, 1 for asynchronous reset, 2 for synchronous reset
  * @param op_iso_mode Operation isolation mode
  */
class CW_sqrt_pipe(
  val width:       Int = 2,
  val tc_mode:     Int = 0,
  val num_stages:  Int = 2,
  val stall_mode:  Int = 1,
  val rst_mode:    Int = 1,
  val op_iso_mode: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "tc_mode" -> tc_mode,
        "num_stages" -> num_stages,
        "stall_mode" -> stall_mode,
        "rst_mode" -> rst_mode,
        "op_iso_mode" -> op_iso_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 2, s"width must be >= 2, but got $width")
  require(tc_mode >= 0 && tc_mode <= 1, s"tc_mode should be in range [0, 1], but got $tc_mode")
  require(num_stages >= 2, s"num_stages must be >= 2, but got $num_stages")
  require(stall_mode >= 0 && stall_mode <= 1, s"stall_mode should be in range [0, 1], but got $stall_mode")
  require(rst_mode >= 0 && rst_mode <= 2, s"rst_mode should be in range [0, 2], but got $rst_mode")
  require(op_iso_mode >= 0 && op_iso_mode <= 4, s"op_iso_mode should be in range [0, 4], but got $op_iso_mode")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:   Clock = Input(Clock())
    val rst_n: Bool  = Input(Bool())
    val en:    Bool  = Input(Bool())
    val a:     UInt  = Input(UInt(width.W))
    val root:  UInt  = Output(UInt(((width + 1) / 2).W))
  })
}
