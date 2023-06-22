import chisel3._
import chisel3.experimental._

/**
 * == CW_fp_div ==
 *
 * === Abstract ===
 *
 * Floating Point Divider
 *
 * === Parameters ===
 *
 * | Parameter         | Values      | Description                                                                                         |
 * |-------------------|-------------|-----------------------------------------------------------------------------------------------------|
 * | sig_width         | 23 bits     | Word length of fraction field of floating-point numbers a,b, and z.                                  |
 * | exp_width         | 8 bits      | Word length of biased exponent of floating point numbers a,b, and z;                                 |
 * | ieee_compliance   | 0 or 1      | When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of   |
 * |                   |             | denormals and NaNs.                                                                                 |
 *
 * === Ports ===
 *
 * | Name   | Width                     | Direction | Description                   |
 * |--------|---------------------------|-----------|-------------------------------|
 * | a      | sig_width+exp_width+1 bits| Input     | Input data                    |
 * | b      | sig_width+exp_width+1 bits| Input     | Input data                    |
 * | rnd    | 3 bits                    | Input     | Rounding mode                 |
 * | status | 8 bits                    | Output    | Status flags                  |
 * | z      | sig_width+exp_width+1 bits| Output    | a / b                         |
 *
 * @param sig_width Word length of fraction field of floating-point numbers a,b, and z.
 * @param exp_width Word length of biased exponent of floating point numbers a,b, and z;
 * @param ieee_compliance When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of denormals and NaNs.
 */
class CW_fp_div(val sig_width: Int = 23,
                val exp_width: Int = 8,
                val ieee_compliance: Int = 1) extends BlackBox(Map(
  "sig_width" -> sig_width,
  "exp_width" -> exp_width,
  "ieee_compliance" -> ieee_compliance
)) {
  // Validation of all parameters
  require(sig_width >= 5 && sig_width <= 23, s"Invalid value ($sig_width) for parameter sig_width (valid value: 5 to 23)")
  require(exp_width >= 5 && exp_width <= 8, s"Invalid value ($exp_width) for parameter exp_width (valid value: 5 to 8)")
  require(Set(0, 1).contains(ieee_compliance), s"Invalid value ($ieee_compliance) for parameter ieee_compliance (valid range: 0 or 1)")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd: UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z: UInt = Output(UInt((sig_width + exp_width + 1).W))
  })
}
