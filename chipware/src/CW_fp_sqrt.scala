import chisel3._
import chisel3.experimental._

/**
  * == CW_fp_sqrt ==
  *
  * === Abstract ===
  *
  * ...
  *
  * === Parameters ===
  *
  * | Parameter          | Legal Range | Default | Description                         |
  * |--------------------|-------------|---------|-------------------------------------|
  * | sig_width          | 2 to 253    | 23      | Width of the significand             |
  * | exp_width          | 3 to 31     | 8       | Width of the exponent                |
  * | ieee_compliance    | 0, 1, or 3  | 1       | IEEE compliance (0, 1, or 3)         |
  *
  * === Ports ===
  *
  * | Name   | Width                  | Direction | Description                         |
  * |--------|------------------------|-----------|-------------------------------------|
  * | a      | sig_width+exp_width+1  | Input     | Input signal                        |
  * | rnd    | 2                      | Input     | Randomization control               |
  * | status | 8                      | Output    | Status bits                         |
  * | z      | sig_width+exp_width+1  | Output    | Output signal                       |
  *
  * @param sig_width        Width of the significand
  * @param exp_width        Width of the exponent
  * @param ieee_compliance  IEEE compliance (0, 1, or 3)
  */
class CW_fp_sqrt(val sig_width: Int = 23, val exp_width: Int = 8, val ieee_compliance: Int = 1)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "ieee_compliance" -> ieee_compliance
      )
    ) {
  // Validations
  require(sig_width >= 2 && sig_width <= 253, "sig_width must be between 2 and 253")
  require(exp_width >= 3 && exp_width <= 31, "exp_width must be between 3 and 31")

  protected val bit_width_wA: Int = sig_width + exp_width + 1

  val io = IO(new Bundle {
    val a:      UInt = Input(UInt(bit_width_wA.W))
    val rnd:    UInt = Input(UInt(2.W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt(bit_width_wA.W))
  })
}
