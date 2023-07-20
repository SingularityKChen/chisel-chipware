import chisel3._
import chisel3.util.{log2Ceil, HasBlackBoxPath}
import chisel3.experimental._

/**
  * == CW_fp_exp ==
  *
  * === Abstract ===
  *
  * Floating Point Natural Exponential
  *
  * === Parameters ===
  *
  * | Parameter        | Legal Range     | Default       | Description                        |
  * |------------------|-----------------|---------------|------------------------------------|
  * | sig_width        | 2 to 112 bits   |               | Word length of fraction field of floating-point numbers `a` and `z`. |
  * | exp_width        | 3 to 31 bits    |               | Word length of biased exponent of floating-point numbers `a` and `z`. |
  * | ieee_compliance  | 0 or 1          |               | When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of denormals and NaNs. |
  * | arch             |                 |               |                                      |
  *
  * === Ports ===
  *
  * | Name   | Width                       | Direction | Description            |
  * |--------|-----------------------------|-----------|------------------------|
  * | a      | sig_width+exp_width+1 bits  | Input     | Input data             |
  * | status | 8 bits                      | Output    | Status flags           |
  * | z      | sig_width+exp_width+1 bits  | Output    | exp(a)                 |
  */
class CW_fp_exp(val sig_width: Int = 23, val exp_width: Int = 8, val ieee_compliance: Int = 1, val arch: Int = 0)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "ieee_compliance" -> ieee_compliance,
        "arch" -> arch
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(sig_width >= 2 && sig_width <= 112, "sig_width must be between 2 and 112")
  require(exp_width >= 3 && exp_width <= 31, "exp_width must be between 3 and 31")
  require(ieee_compliance == 0 || ieee_compliance == 1, "ieee_compliance must be either 0 or 1")
  // No validation needed for arch parameter

  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt((sig_width + exp_width + 1).W))
  })
}
