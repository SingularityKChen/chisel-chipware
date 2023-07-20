// filename: CW_mult_pipe.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_mult_pipe ==
  *
  * === Abstract ===
  *
  * CW_mult_pipe is an n stage stallable pipelined multipler
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | a_width       | >= 1         | -            | Word length of a |
  * | b_width       | >= 1         | -            | Word length of b |
  * | num_stages    | >= 1         | -            | Number of pipelined stages |
  * | stall_mode    | 0 or 1       | -            | Stall mode |
  * | rst_mode      | 0 to 2       | -            | Reset mode |
  * | op_iso_mode   | 0 to 4       | -            | Op iso mode |
  *
  * === Ports ===
  *
  * | Name     | Width             | Direction | Description  |
  * |----------|------------------|-----------|--------------|
  * | clk      | 1                 | Input     | Clock        |
  * | rst_n    | 1                 | Input     | Reset, active low |
  * | en       | 1                 | Input     | Load enable, active low |
  * | tc       | 1                 | Input     | 2's complement control |
  * | a        | a_width           | Input     | Multiplier |
  * | b        | b_width           | Input     | Multiplicand |
  * | product  | a_width+b_width-1 | Output    | Product (a*b) |
  *
  * @param a_width    Word length of a
  * @param b_width    Word length of b
  * @param num_stages Number of pipelined stages
  * @param stall_mode Stall mode
  * @param rst_mode   Reset mode
  * @param op_iso_mode Op iso mode
  */
class CW_mult_pipe(
  val a_width:     Int = 2,
  val b_width:     Int = 2,
  val num_stages:  Int = 2,
  val stall_mode:  Int = 1,
  val rst_mode:    Int = 1,
  val op_iso_mode: Int = 0)
    extends BlackBox(
      Map(
        "a_width" -> a_width,
        "b_width" -> b_width,
        "num_stages" -> num_stages,
        "stall_mode" -> stall_mode,
        "rst_mode" -> rst_mode,
        "op_iso_mode" -> op_iso_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(a_width >= 1, s"a_width must be >= 1, but got $a_width")
  require(b_width >= 1, s"b_width must be >= 1, but got $b_width")
  require(num_stages >= 1, s"num_stages must be >= 1, but got $num_stages")
  require(stall_mode >= 0 && stall_mode <= 1, s"stall_mode should be 0 or 1, but got $stall_mode")
  require(rst_mode >= 0 && rst_mode <= 2, s"rst_mode should be in range [0, 2], but got $rst_mode")
  require(op_iso_mode >= 0 && op_iso_mode <= 4, s"op_iso_mode should be in range [0, 4], but got $op_iso_mode")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:     Clock = Input(Clock())
    val rst_n:   Bool  = Input(Bool())
    val en:      Bool  = Input(Bool())
    val tc:      Bool  = Input(Bool())
    val a:       UInt  = Input(UInt(a_width.W))
    val b:       UInt  = Input(UInt(b_width.W))
    val product: UInt  = Output(UInt((a_width + b_width - 1).W))
  })
}
