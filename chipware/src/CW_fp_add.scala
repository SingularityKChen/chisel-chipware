import chisel3._
import chisel3.experimental._

/**
  * == CW_fp_add ==
  *
  * === Abstract ===
  *
  * Floating Point Adder
  *
  * === Parameters ===
  *
  * | Parameter           | Legal Range  | Default  | Description                                        |
  * |---------------------|--------------|----------|----------------------------------------------------|
  * | sig_width           | 5 to 64      | -        | Word length of fraction field of floating-point numbers a, b, and z.          |
  * | exp_width           | 5 to 15      | -        | Word length of biased exponent of floating-point numbers a, b, and z.          |
  * | ieee_compliance     | 0 or 1       | -        | When 1, the generated architecture is fully compliant with IEEE 754 standard, including the use of denormals and NaNs. |
  * | ieee_NaN_compliance | -            | -        | Not specified in the Verilog module.                |
  * | arch                | 0 or 1       | -        | Setting to 1 when ieee_compliance is 0 provides a faster architecture. |
  *
  * === Ports ===
  *
  * | Name   | Width                      | Direction | Description         |
  * |--------|----------------------------|-----------|---------------------|
  * | a      | sig_width+exp_width+1 bits | Input     | Input data          |
  * | b      | sig_width+exp_width+1 bits | Input     | Input data          |
  * | rnd    | 3 bits                     | Input     | Rounding mode       |
  * | status | 8 bits                     | Output    | Status flags        |
  * | z      | sig_width+exp_width+1 bits | Output    | a + b               |
  *
  * @param sig_width           Word length of fraction field of floating-point numbers a, b, and z.
  * @param exp_width           Word length of biased exponent of floating-point numbers a, b, and z.
  * @param ieee_compliance     When 1, the generated architecture is fully compliant with IEEE 754 standard, including the use of denormals and NaNs.
  * @param ieee_NaN_compliance Not specified in the Verilog module.
  * @param arch                Setting to 1 when ieee_compliance is 0 provides a faster architecture.
  */
class CW_fp_add(
  val sig_width:           Int = 23,
  val exp_width:           Int = 8,
  val ieee_compliance:     Int = 1,
  val ieee_NaN_compliance: Int = 0,
  val arch:                Int = 0)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "ieee_compliance" -> ieee_compliance,
        "ieee_NaN_compliance" -> ieee_NaN_compliance,
        "arch" -> arch
      )
    ) {
  // Validations for parameters
  require(sig_width >= 5 && sig_width <= 64, s"sig_width must be in the range [5, 64]")
  require(exp_width >= 5 && exp_width <= 15, s"exp_width must be in the range [5, 15]")
  require(List(0, 1).contains(ieee_compliance), "ieee_compliance must be 0 or 1")
  //require(ieee_NaN_compliance, "ieee_NaN_compliance must be specified") // Uncomment this line if ieee_NaN_compliance is specified

  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd:    UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt((sig_width + exp_width + 1).W))
  })
}
