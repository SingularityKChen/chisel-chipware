import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_div_sat ==
  *
  * === Abstract ===
  *
  * Integer divider with saturation
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | a_width | >= 2 | 8 | Dividend bitwidth |
  * | b_width | >= 2 | 8 | Divisor and modulus/remainder bitwidth |
  * | q_width | >= 2 | 8 | Quotient bitwidth |
  * | tc_mode | 0 to 1 | 0 | 0: Input and output values are unsigned numbers. 1: Input and output values are signed numbers. |
  * | rem_mode | 0 to 1 | 1 | 0: Modulus mode. 1: Remainder mode. |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | a  | a_width  | Input | Dividend |
  * | b  | b_width  | Input | Divisor |
  * | quotient  | q_width  | Output | Quotient |
  * | remainder  | b_width  | Output | Remainder/Modulus |
  * | divide_by_0  | 1  | Output | Divide by zero flag |
  * | saturation  | 1  | Output | Saturation flag |
  *
  * @param a_width Dividend bitwidth
  * @param b_width Divisor and modulus/remainder bitwidth
  * @param q_width Quotient bitwidth
  * @param tc_mode 0: Input and output values are unsigned numbers. 1: Input and output values are signed numbers.
  * @param rem_mode 0: Modulus mode. 1: Remainder mode.
  */
class CW_div_sat(
  val a_width:  Int = 8,
  val b_width:  Int = 8,
  val q_width:  Int = 8,
  val tc_mode:  Int = 0,
  val rem_mode: Int = 1)
    extends BlackBox(
      Map(
        "a_width" -> a_width,
        "b_width" -> b_width,
        "q_width" -> q_width,
        "tc_mode" -> tc_mode,
        "rem_mode" -> rem_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(a_width >= 2, s"a_width must be >= 2, got $a_width")
  require(b_width >= 2, s"b_width must be >= 2, got $b_width")
  require(q_width >= 2, s"q_width must be >= 2, got $q_width")
  require(tc_mode >= 0 && tc_mode <= 1, s"tc_mode should be in range [0, 1], got $tc_mode")
  require(rem_mode >= 0 && rem_mode <= 1, s"rem_mode should be in range [0, 1], got $rem_mode")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:           UInt = Input(UInt(a_width.W))
    val b:           UInt = Input(UInt(b_width.W))
    val quotient:    UInt = Output(UInt(q_width.W))
    val remainder:   UInt = Output(UInt(b_width.W))
    val divide_by_0: Bool = Output(Bool())
    val saturation:  Bool = Output(Bool())
  })
}
