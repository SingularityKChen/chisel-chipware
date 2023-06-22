import chisel3._
import chisel3.experimental._

// ScalaDoc before the definition of the Chisel BlackBox class
/**
 * == CW_fp_log2 ==
 *
 * === Abstract ===
 *
 * Floating Point Base-2 Logarithm
 *
 * === Parameters ===
 *
 * | Parameter         | Legal Range            | Default | Description                                                      |
 * |-------------------|------------------------|---------|------------------------------------------------------------------|
 * | sig_width         | 10 to 23 bits          | 23      | Word length of fraction field of floating-point numbers a and z. |
 * | exp_width         | 5 to 8 bits            | 8       | Word length of biased exponent of floating point numbers a and z; |
 * | ieee_compliance   | 0 or 1                 | 1       | When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of denormals and NaNs. |
 * | arch              |                        | 0       |                                                                  |
 * | extra_prec        |                        | 0       |                                                                  |
 *
 * === Ports ===
 *
 * | Name    | Width                           | Direction | Description    |
 * |---------|---------------------------------|-----------|----------------|
 * | a       | sig_width+exp_width+1 bits      | Input     | Input data     |
 * | status  | 8 bits                          | Output    | Status flags   |
 * | z       | sig_width+exp_width+1 bits      | Output    | log2(a)        |
 *
 * @param sig_width        Word length of fraction field of floating-point numbers a and z.
 * @param exp_width        Word length of biased exponent of floating point numbers a and z;
 * @param ieee_compliance  When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of denormals and NaNs.
 * @param arch             Unused parameter
 * @param extra_prec       Unused parameter
 */
class CW_fp_log2(val sig_width: Int = 23,
                 val exp_width: Int = 8,
                 val ieee_compliance: Int = 1,
                 val arch: Int = 0,
                 val extra_prec: Int = 0) extends BlackBox(Map(
  "sig_width" -> sig_width,
  "exp_width" -> exp_width,
  "ieee_compliance" -> ieee_compliance,
  "arch" -> arch,
  "extra_prec" -> extra_prec
)) {
  require(Set(0, 1, 3).contains(ieee_compliance), "Invalid value for parameter ieee_compliance")
  require(sig_width >= 10 && sig_width <= 23, "Invalid value for parameter sig_width")
  require(sig_width <= math.pow(2, exp_width - 1) - 3, "Invalid value for parameter sig_width")
  require(exp_width >= 5 && exp_width <= 8, "Invalid value for parameter exp_width")

  // Define ports
  val io = IO(new Bundle {
    val a: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val status: UInt = Output(UInt(8.W))
    val z: UInt = Output(UInt((sig_width + exp_width + 1).W))
  })

}
