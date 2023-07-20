import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_prod_sum_pipe ==
  *
  * === Abstract ===
  *
  * Stallable Pipelined Generalized Sum of Products Generator
  *
  * === Parameters ===
  *
  * | Parameter   | Legal Range | Default | Description                      |
  * |-------------|-------------|---------|----------------------------------|
  * | a_width     | >= 1        | 2       | Word length of a                 |
  * | b_width     | >= 1        | 2       | Word length of b                 |
  * | num_inputs  | >= 1        | 2       | Number of inputs                 |
  * | sum_width   | >= 1        | 4       | Word length of SUM               |
  * | num_stages  | >= 2        | 2       | Number of pipelined stages       |
  * | stall_mode  | 0 or 1      | 1       | Stall mode (0 = non-stallable)   |
  * | rst_mode    | 0 to 2      | 1       | Reset mode (0 = no reset)        |
  * | op_iso_mode | 0 to 4      | 0       | Operational isolation mode       |
  *
  * === Ports ===
  *
  * | Name  | Width                 | Direction | Description                 |
  * |-------|-----------------------|-----------|-----------------------------|
  * | clk   | 1                     | Input     | Clock                       |
  * | rst_n | 1                     | Input     | Reset, active low           |
  * | en    | 1                     | Input     | Load enable (used only if parameter stall_mode=1). 0 = stall, 1 = load |
  * | tc    | 1                     | Input     | Control Input: 0 = Number is unsigned,  1 = Number is signed |
  * | a     | a_width * num_inputs  | Input     | Multiplier                  |
  * | b     | b_width * num_inputs  | Input     | Multiplicand                |
  * | sum   | sum_width             | Output    | Sum                         |
  *
  * @param a_width Word length of a
  * @param b_width Word length of b
  * @param num_inputs Number of inputs
  * @param sum_width Word length of SUM
  * @param num_stages Number of pipelined stages
  * @param stall_mode Stall mode (0 = non-stallable)
  * @param rst_mode Reset mode (0 = no reset)
  * @param op_iso_mode Operational isolation mode
  */
class CW_prod_sum_pipe(
  val a_width:     Int = 2,
  val b_width:     Int = 2,
  val num_inputs:  Int = 2,
  val sum_width:   Int = 4,
  val num_stages:  Int = 2,
  val stall_mode:  Int = 1,
  val rst_mode:    Int = 1,
  val op_iso_mode: Int = 0)
    extends BlackBox(
      Map(
        "a_width" -> a_width,
        "b_width" -> b_width,
        "num_inputs" -> num_inputs,
        "sum_width" -> sum_width,
        "num_stages" -> num_stages,
        "stall_mode" -> stall_mode,
        "rst_mode" -> rst_mode,
        "op_iso_mode" -> op_iso_mode
      )
    )
    with HasBlackBoxPath {
  require(a_width >= 1, "a_width must be >= 1")
  require(b_width >= 1, "b_width must be >= 1")
  require(num_inputs >= 1, "num_inputs must be >= 1")
  require(sum_width >= 1, "sum_width must be >= 1")
  require(num_stages >= 2, "num_stages must be >= 2")
  require(stall_mode == 0 || stall_mode == 1, "stall_mode must be 0 or 1")
  require(rst_mode >= 0 && rst_mode <= 2, "rst_mode must be in range [0, 2]")
  require(op_iso_mode >= 0 && op_iso_mode <= 4, "op_iso_mode must be in range [0, 4]")

  val io = IO(new Bundle {
    val clk:   Clock = Input(Clock())
    val rst_n: Bool  = Input(Bool())
    val en:    Bool  = Input(Bool())
    val tc:    Bool  = Input(Bool())
    val a:     UInt  = Input(UInt((a_width * num_inputs).W))
    val b:     UInt  = Input(UInt((b_width * num_inputs).W))
    val sum:   UInt  = Output(UInt(sum_width.W))
  })
}
