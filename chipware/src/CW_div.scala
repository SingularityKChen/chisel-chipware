import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_div ==
  *
  * === Abstract ===
  *
  * Combinational Divider with Remainder
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | a_width       | >= 2         | 8            | Width of numerator |
  * | b_width       | >= 2         | 8            | Width of denominator |
  * | tc_mode       | 0 or 1       | 0            | 0: a and b are interpreted as unsigned integers 1: a and b are interpreted as signed integers |
  * | rem_mode      | 0 or 1       | 1            | 0: Modulus 1: Remainder |
  * | arch          | 0       | 0            | Architecture selection |
  *
  * === Ports ===
  *
  * | Name          | Width        | Direction | Description  |
  * |---------------|--------------|-----------|----------------|
  * | a             | a_width      | Input     | Numerator |
  * | b             | b_width      | Input     | Denominator |
  * | quotient      | a_width      | Output    | Quotient |
  * | remainder     | b_width      | Output    | Remainder |
  * | divide_by_0   | 1            | Output    | Divide by zero error flag |
  *
  * @param a_width Width of numerator
  * @param b_width Width of denominator
  * @param tc_mode 0: a and b are interpreted as unsigned integers 1: a and b are interpreted as signed integers
  * @param rem_mode 0: Modulus 1: Remainder
  * @param arch Architecture selection
  */
class CW_div(val a_width: Int = 8, val b_width: Int = 8, val tc_mode: Int = 0, val rem_mode: Int = 1, val arch: Int = 0)
    extends BlackBox(
      Map(
        "a_width" -> a_width,
        "b_width" -> b_width,
        "tc_mode" -> tc_mode,
        "rem_mode" -> rem_mode,
        "arch" -> arch
      )
    )
    with HasBlackBoxPath {
  require(a_width >= 2, "a_width must be >= 2")
  require(b_width >= 2, "b_width must be >= 2")
  require(tc_mode == 0 || tc_mode == 1, "tc_mode should be 0 or 1")
  require(rem_mode == 0 || rem_mode == 1, "rem_mode should be 0 or 1")
  require(arch == 0, "arch should be 0")

  val io = IO(new Bundle {
    val a:           UInt = Input(UInt(a_width.W))
    val b:           UInt = Input(UInt(b_width.W))
    val quotient:    UInt = Output(UInt(a_width.W))
    val remainder:   UInt = Output(UInt(b_width.W))
    val divide_by_0: Bool = Output(Bool())
  })
}
