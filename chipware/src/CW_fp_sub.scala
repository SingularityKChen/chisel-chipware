import chisel3._
import chisel3.experimental._

/**
 * == CW_fp_sub ==
 *
 * === Abstract ===
 *
 * Floating Point Subtractor
 *
 * === Parameters ===
 *
 * | Parameter         | Legal Range     | Default         | Description                                                   |
 * |-------------------|-----------------|-----------------|---------------------------------------------------------------|
 * | sig_width         | 5 to 64 bits    |                 | Word length of fraction field of floating-point numbers a, b, and z.   |
 * | exp_width         | 5 to 15 bits    |                 | Word length of biased exponent of floating point numbers a, b, and z; |
 * | ieee_compliance   | 0 or 1          |                 | When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of denormals and NaNs. |
 *
 * === Ports ===
 *
 * | Name      | Width                    | Direction | Description        |
 * |-----------|--------------------------|-----------|--------------------|
 * | a         | sig_width+exp_width+1    | Input     | Input data         |
 * | b         | sig_width+exp_width+1    | Input     | Input data         |
 * | rnd       | 3                        | Input     | Rounding mode      |
 * | status    | 8                        | Output    | Status flags       |
 * | z         | sig_width+exp_width+1    | Output    | a - b              |
 *
 * @param sig_width Word length of fraction field of floating-point numbers a, b, and z.
 * @param exp_width Word length of biased exponent of floating point numbers a, b, and z;
 * @param ieee_compliance When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of denormals and NaNs.
 */
class CW_fp_sub(val sig_width: Int = 23,
                val exp_width: Int = 8,
                val ieee_compliance: Int = 1) extends BlackBox(Map(
  "sig_width" -> sig_width,
  "exp_width" -> exp_width,
  "ieee_compliance" -> ieee_compliance
)) {
  require(sig_width >= 5 && sig_width <= 64, "sig_width must be between 5 and 64")
  require(exp_width >= 5 && exp_width <= 15, "exp_width must be between 5 and 15")

  val io = IO(new Bundle {
    val a: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd: UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z: UInt = Output(UInt((sig_width + exp_width + 1).W))
  })
}
