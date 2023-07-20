import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_fp_recip ==
  *
  * === Abstract ===
  *
  * Returns the reciprocal of a floating-point number 'a' with rounding dependent on 'rnd'.
  * 'a' is a floating-point number with the given exponent width and mantissa width.
  * IEEE compliance is supported via an option flag.
  *
  * === Parameters ===
  *
  * | Parameter         | Legal Range    | Default | Description                                          |
  * |-------------------|----------------|---------|------------------------------------------------------|
  * | sig_width         | 2 to 253 bits  | 23      | Half => 10, Single => 23, Double => 52               |
  * | exp_width         | 3 to 31 bits   | 8       | Half => 5, Single => 8, Double => 11                 |
  * | ieee_compliance   | 0, 1, or 3     | 1       | Non-compliant = no denormals and limited NAN support |
  * | faithful_round    | 0 or 1         | 0       | Support for faithful rounding (only for arch 0)      |
  * | arch              | 0 or 1         | 0       | Architecture selection (0 is optimized for speed)    |
  *
  * === Ports ===
  *
  * | Name    | Width                            | Direction | Description    |
  * |---------|----------------------------------|-----------|----------------|
  * | a       | sig_width+exp_width bits         | Input     | Input data     |
  * | rnd     | 3 bits                           | Input     | Input data     |
  * | status  | 8 bits                           | Output    | Status flags   |
  * | z       | sig_width+exp_width bits         | Output    | Result         |
  *
  * @param sig_width        Half => 10, Single => 23, Double => 52
  * @param exp_width        Half => 5, Single => 8, Double => 11
  * @param ieee_compliance  Non-compliant = no denormals and limited NAN support
  * @param faithful_round   Support for faithful rounding (only for arch 0)
  * @param arch             Architecture selection (0 is optimized for speed)
  */
class CW_fp_recip(
  val sig_width:       Int = 23,
  val exp_width:       Int = 8,
  val ieee_compliance: Int = 1,
  val faithful_round:  Int = 0,
  val arch:            Int = 0)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "ieee_compliance" -> ieee_compliance,
        "faithful_round" -> faithful_round,
        "arch" -> arch
      )
    )
    with HasBlackBoxPath {
  require((sig_width >= 2) && (sig_width <= 253), "sig_width must be between 2 and 253")
  require((exp_width >= 3) && (exp_width <= 31), "exp_width must be between 3 and 31")
  require(Seq(0, 1, 3).contains(ieee_compliance), "ieee_compliance must be 0, 1, or 3")
  require(Seq(0, 1).contains(faithful_round), "faithful_round must be 0 or 1")
  require(Seq(0, 1).contains(arch), "arch must be 0 or 1")

  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width).W))
    val rnd:    UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt((sig_width + exp_width).W))
  })

  // Simulation parameter validation
  if (faithful_round == 1) {
    assert(sig_width == 23, "Faithful rounding is allowed only for sig_width = 23")
    assert(exp_width == 8, "Faithful rounding is allowed only for exp_width = 8")
    assert(ieee_compliance == 0, "Faithful rounding is allowed only for ieee_compliance = 0")
    assert(arch == 0, "Faithful rounding is allowed only for arch = 0")
  }
}
