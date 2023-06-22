import chisel3._
import chisel3.experimental._ // To enable experimental features

/**
 * == CW_fp_sincos ==
 *
 * === Abstract ===
 *
 * ...
 *
 * === Parameters ===
 *
 * | Parameter         | Legal Range  | Default      | Description         |
 * |-------------------|--------------|--------------|---------------------|
 * | sig_width         | 2 to 33      |              | Range of signal     |
 * | exp_width         | 3 to 31      |              | Range of exponent   |
 * | op_width          |              | 1 + exp_width + sig_width | Width of operand |
 * | ieee_compliance   | 0, 1, or 3   | 1            | IEEE compliance     |
 * | pi_multiple       | 0 or 1       | 1            | PI multiple         |
 *
 * === Ports ===
 *
 * | Name      | Width        | Direction | Description            |
 * |-----------|--------------|-----------|------------------------|
 * | a         | op_width     | Input     | Input operand          |
 * | sin_cos   |              | Input     | Input signal           |
 * | z         | op_width     | Output    | Output result          |
 * | status    | 8            | Output    | Status information     |
 *
 * @param sig_width       Range of signal
 * @param exp_width       Range of exponent
 * @param ieee_compliance IEEE compliance
 * @param pi_multiple     PI multiple
 */
class CW_fp_sincos(
                    val sig_width: Int = 33,
                    val exp_width: Int = 6,
                    val ieee_compliance: Int = 1,
                    val pi_multiple: Int = 1
                  ) extends BlackBox(Map(
  "sig_width" -> sig_width,
  "exp_width" -> exp_width,
  "op_width" -> (1 + exp_width + sig_width),
  "ieee_compliance" -> ieee_compliance,
  "pi_multiple" -> pi_multiple
)) {
  require(sig_width >= 2 && sig_width <= 33, "sig_width must be in range [2, 33]")
  require(exp_width >= 3 && exp_width <= 31, "exp_width must be in range [3, 31]")
  require((pi_multiple == 0 || pi_multiple == 1), "pi_multiple must be either 0 or 1")

  val io = IO(new Bundle {
    val a: UInt = Input(UInt((1 + exp_width + sig_width).W))
    val sin_cos: Bool = Input(Bool())
    val z: UInt = Output(UInt((1 + exp_width + sig_width).W))
    val status: UInt = Output(UInt(8.W))
  })
}
