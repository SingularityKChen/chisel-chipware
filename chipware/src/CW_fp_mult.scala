import chisel3._
import chisel3.experimental._ // To enable experimental features

/**
  * CW_fp_mult
  *
  * === Parameters ===
  * | Parameter       | Description                                                                     |
  * |-----------------|---------------------------------------------------------------------------------|
  * | sig_width       | Word length of fraction field of floating-point numbers a, b, and z.            |
  * | exp_width       | Word length of biased exponent of floating-point numbers a, b, and z.           |
  * | ieee_compliance | When 1, the generated architecture is fully compliant with IEEE 754 standard.   |
  * | arch            | Setting to 1 when ieee_compliance is 0 provides a faster architecture.          |
  *
  * === Ports ===
  * | Port   | Bit Width                       | Direction | Function                     |
  * |--------|---------------------------------|-----------|------------------------------|
  * | a      | sig_width + exp_width + 1 bits | Input     | Input data                   |
  * | b      | sig_width + exp_width + 1 bits | Input     | Input data                   |
  * | rnd    | 3 bits                          | Input     | Rounding mode                |
  * | status | 8 bits                          | Output    | Status flags                 |
  * | z      | sig_width + exp_width + 1 bits | Output    | a * b                        |
  *
  * @param sig_width       Word length of fraction field of floating-point numbers a, b, and z.
  * @param exp_width       Word length of biased exponent of floating-point numbers a, b, and z.
  * @param ieee_compliance When 1, the generated architecture is fully compliant with IEEE 754 standard.
  * @param arch            Setting to 1 when ieee_compliance is 0 provides a faster architecture.
  */
class CW_fp_mult(val sig_width: Int = 23, val exp_width: Int = 8, val ieee_compliance: Int = 1, val arch: Int = 0)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "ieee_compliance" -> ieee_compliance,
        "arch" -> arch
      )
    ) {
  require(sig_width >= 2 && sig_width <= 112, "sig_width must be in the range [2, 112]")
  require(exp_width >= 3 && exp_width <= 15, "exp_width must be in the range [3, 15]")
  require(Seq(0, 1).contains(ieee_compliance), "ieee_compliance must be either 0 or 1")
  require(Seq(0, 1).contains(arch), "arch must be either 0 or 1")

  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd:    UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt((sig_width + exp_width + 1).W))
  })
}
