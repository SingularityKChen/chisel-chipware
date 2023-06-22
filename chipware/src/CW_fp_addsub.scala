import chisel3._
import chisel3.experimental._ // To enable experimental features
/**
 * == CW_fp_addsub ==
 *
 * === Abstract ===
 *
 * Floating Point Adder/Subtractor
 *
 * === Parameters ===
 *
 * | Parameter           | Legal Range  | Default  | Description                                        |
 * |---------------------|--------------|----------|----------------------------------------------------|
 * | sig_width           | 5 to 64      | -        | Word length of fraction field of floating-point numbers a, b, and z.          |
 * | exp_width           | 5 to 15      | -        | Word length of biased exponent of floating-point numbers a, b, and z.          |
 * | ieee_compliance     | 0 or 1       | -        | When 1, the generated architecture is fully compliant with IEEE 754 standard, including the use of denormals and NaNs. |
 *
 * === Ports ===
 *
 * | Name   | Width                      | Direction | Description         |
 * |--------|----------------------------|-----------|---------------------|
 * | a      | sig_width+exp_width+1 bits | Input     | Input data          |
 * | b      | sig_width+exp_width+1 bits | Input     | Input data          |
 * | rnd    | 3 bits                     | Input     | Rounding mode       |
 * | op     | 1 bit                      | Input     | Operation mode      |
 * | status | 8 bits                     | Output    | Status flags        |
 * | z      | sig_width+exp_width+1 bits | Output    | a +- b              |
 *
 * @param sig_width       Word length of fraction field of floating-point numbers a, b, and z.
 * @param exp_width       Word length of biased exponent of floating-point numbers a, b, and z.
 * @param ieee_compliance When 1, the generated architecture is fully compliant with IEEE 754 standard, including the use of denormals and NaNs.
 */
class CW_fp_addsub(val sig_width: Int = 23,
                   val exp_width: Int = 8,
                   val ieee_compliance: Int = 1) extends BlackBox(Map(
  "sig_width" -> sig_width,
  "exp_width" -> exp_width,
  "ieee_compliance" -> ieee_compliance
)) {
  // Validations for parameters
  require(sig_width >= 5 && sig_width <= 64, s"sig_width must be in the range [5, 64]")
  require(exp_width >= 5 && exp_width <= 15, s"exp_width must be in the range [5, 15]")
  require(List(0, 1, 3).contains(ieee_compliance), "ieee_compliance must be 0, 1, or 3")

  val io = IO(new Bundle {
    val a: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd: UInt = Input(UInt(3.W))
    val op: Bool = Input(Bool())
    val status: UInt = Output(UInt(8.W))
    val z: UInt = Output(UInt((sig_width + exp_width + 1).W))
  })
}
