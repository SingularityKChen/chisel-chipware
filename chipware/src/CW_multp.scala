import chisel3._
import chisel3.experimental._

/**
  * == CW_multp ==
  *
  * === Abstract ===
  *
  * Partial Product Multiplier
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | a_width  | >= 1  | 8 | Width of input a |
  * | b_width  | >= 1  | 8 | Width of input b |
  * | out_width  | >= a_width+b_width+2  | 18 | Width of output |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | a  | a_width  | Input | Input a |
  * | b  | b_width  | Input | Input b |
  * | tc  | 1  | Input | Test control |
  * | out0  | out_width  | Output | Output 0 |
  * | out1  | out_width  | Output | Output 1 |
  *
  * @param a_width Width of input a
  * @param b_width Width of input b
  * @param out_width Width of output
  */
class CW_multp(val a_width: Int = 8, val b_width: Int = 8, val out_width: Int = 18)
    extends BlackBox(
      Map(
        "a_width" -> a_width,
        "b_width" -> b_width,
        "out_width" -> out_width
      )
    ) {
  require(a_width >= 1, s"a_width must be >= 1, got $a_width")
  require(b_width >= 1, s"b_width must be >= 1, got $b_width")
  require(out_width >= a_width + b_width + 2, s"out_width must be >= a_width + b_width + 2, got $out_width")
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(a_width.W))
    val b:    UInt = Input(UInt(b_width.W))
    val tc:   Bool = Input(Bool())
    val out0: UInt = Output(UInt(out_width.W))
    val out1: UInt = Output(UInt(out_width.W))
  })
}
