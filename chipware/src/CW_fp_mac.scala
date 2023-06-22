import chisel3._
import chisel3.experimental._

// ScalaDoc before the definition of the Chisel BlackBox class
/**
 * == CW_fp_mac ==
 *
 * === Abstract ===
 *
 * Returns z = (a * b) + c (rounding dependent on rnd)
 * z, a, b, and c are all floating-point numbers with the given exponent width and mantissa width.
 * IEEE compliance is supported via an option flag.
 * Valid for 3 <= exp_width <= 8 and 2 <= sig_width <= 23.
 *
 * === Parameters ===
 *
 * | Parameter         | Legal Range    | Default | Description                                          |
 * |-------------------|----------------|---------|------------------------------------------------------|
 * | sig_width         | 2 to 23 bits   | 23      | Half => 10, Single => 23, Double => 52               |
 * | exp_width         | 3 to 8 bits    | 8       | Half => 5, Single => 8, Double => 11                 |
 * | ieee_compliance   | 0 or 1         | 1       | Non-compliant = no denormals and limited NAN support |
 *
 * === Ports ===
 *
 * | Name    | Width                            | Direction | Description    |
 * |---------|----------------------------------|-----------|----------------|
 * | a       | sig_width+exp_width bits         | Input     | Input data     |
 * | b       | sig_width+exp_width bits         | Input     | Input data     |
 * | c       | sig_width+exp_width bits         | Input     | Input data     |
 * | rnd     | 3 bits                           | Input     | Input data     |
 * | z       | sig_width+exp_width bits         | Output    | Result         |
 * | status  | 8 bits                           | Output    | Status flags   |
 *
 * @param sig_width        Half => 10, Single => 23, Double => 52
 * @param exp_width        Half => 5, Single => 8, Double => 11
 * @param ieee_compliance  Non-compliant = no denormals and limited NAN support
 */
class CW_fp_mac(val sig_width: Int = 23,
                val exp_width: Int = 8,
                val ieee_compliance: Int = 1) extends BlackBox(Map(
  "sig_width" -> sig_width,
  "exp_width" -> exp_width,
  "ieee_compliance" -> ieee_compliance
)) {
  require((sig_width >= 2) && (sig_width <= 23), "sig_width must be between 2 and 23")
  require((exp_width >= 3) && (exp_width <= 8), "exp_width must be between 3 and 8")

  val io = IO(new Bundle {
    val a: UInt = Input(UInt((sig_width + exp_width).W))
    val b: UInt = Input(UInt((sig_width + exp_width).W))
    val c: UInt = Input(UInt((sig_width + exp_width).W))
    val rnd: UInt = Input(UInt(3.W))
    val z: UInt = Output(UInt((sig_width + exp_width).W))
    val status: UInt = Output(UInt(8.W))
  })
}
