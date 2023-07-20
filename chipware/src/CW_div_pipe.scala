import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_div_pipe ==
  *
  * === Abstract ===
  *
  * CW_div_pipe is an  n stage stallable pipelined divider
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | a_width  | >= 2  | 2 | Word length of a |
  * | b_width  | >= 2 and <= a_width  | 2 | Word length of b |
  * | tc_mode  | 0 or 1  | 0 | Two's complement control: 0 : inputs/outputs unsigned, 1 : inputs/outputs two's complement |
  * | rem_mode  | 0 or 1  | 1 | Remainder output control: 0 : Remainder output is VHDL modulus, 1 : Remainder output is remainder |
  * | num_stages  | >= 2  | 2 | Number of pipelined stages |
  * | stall_mode  | 0 or 1  | 1 | Stall mode (0 = non-stallable, 1 = stallable) |
  * | rst_mode  | 0 to 2  | 1 | Reset mode (0 = no reset, 1 = asynchronous reset, 2 = synchronous reset) |
  * | op_iso_mode  | 0 to 4  | 0 | Select the implementation (0: area-optimized implementation, 1: speed-optimized implementation, 2: LEC's library simulation model compatible) |
  * | arch  | 0 to 2  | 1 | Select the implementation (0: area-optimized implementation, 1: speed-optimized implementation, 2: LEC's library simulation model compatible) |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | clk | 1 | Input | Clock |
  * | rst_n | 1 | Input | Reset, active low |
  * | en | 1 | Input | Load enable, active high |
  * | a | a_width | Input | Divisor |
  * | b | b_width | Input | Dividend |
  * | quotient | a_width | Output | Quotient (a/b) |
  * | remainder | b_width | Output | Remainder |
  * | divide_by_0 | 1 | Output | Divide_by_0 |
  *
  * @param a_width Word length of a
  * @param b_width Word length of b
  * @param tc_mode Two's complement control: 0 : inputs/outputs unsigned, 1 : inputs/outputs two's complement
  * @param rem_mode Remainder output control: 0 : Remainder output is VHDL modulus, 1 : Remainder output is remainder
  * @param num_stages Number of pipelined stages
  * @param stall_mode Stall mode (0 = non-stallable, 1 = stallable)
  * @param rst_mode Reset mode (0 = no reset, 1 = asynchronous reset, 2 = synchronous reset)
  * @param op_iso_mode Select the implementation (0: area-optimized implementation, 1: speed-optimized implementation, 2: LEC's library simulation model compatible)
  * @param arch Select the implementation (0: area-optimized implementation, 1: speed-optimized implementation, 2: LEC's library simulation model compatible)
  */
class CW_div_pipe(
  val a_width:     Int = 2,
  val b_width:     Int = 2,
  val tc_mode:     Int = 0,
  val rem_mode:    Int = 1,
  val num_stages:  Int = 2,
  val stall_mode:  Int = 1,
  val rst_mode:    Int = 1,
  val op_iso_mode: Int = 0,
  val arch:        Int = 1)
    extends BlackBox(
      Map(
        "a_width" -> a_width,
        "b_width" -> b_width,
        "tc_mode" -> tc_mode,
        "rem_mode" -> rem_mode,
        "num_stages" -> num_stages,
        "stall_mode" -> stall_mode,
        "rst_mode" -> rst_mode,
        "op_iso_mode" -> op_iso_mode,
        "arch" -> arch
      )
    )
    with HasBlackBoxPath {
  require(a_width >= 2, s"a_width must be >= 2, but got $a_width")
  require(b_width >= 2 && b_width <= a_width, s"b_width must be >= 2 and <= a_width, but got $b_width")
  require(tc_mode >= 0 && tc_mode <= 1, s"tc_mode must be 0 or 1, but got $tc_mode")
  require(rem_mode >= 0 && rem_mode <= 1, s"rem_mode must be 0 or 1, but got $rem_mode")
  require(num_stages >= 2, s"num_stages must be >= 2, but got $num_stages")
  require(stall_mode >= 0 && stall_mode <= 1, s"stall_mode must be 0 or 1, but got $stall_mode")
  require(rst_mode >= 0 && rst_mode <= 2, s"rst_mode must be 0 to 2, but got $rst_mode")
  require(op_iso_mode >= 0 && op_iso_mode <= 4, s"op_iso_mode must be 0 to 4, but got $op_iso_mode")
  require(arch >= 0 && arch <= 2, s"arch must be 0 to 2, but got $arch")

  val io = IO(new Bundle {
    val clk:         Clock = Input(Clock())
    val rst_n:       Bool  = Input(Bool())
    val en:          Bool  = Input(Bool())
    val a:           UInt  = Input(UInt(a_width.W))
    val b:           UInt  = Input(UInt(b_width.W))
    val quotient:    UInt  = Output(UInt(a_width.W))
    val remainder:   UInt  = Output(UInt(b_width.W))
    val divide_by_0: Bool  = Output(Bool())
  })
}
