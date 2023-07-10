import chisel3._
import chisel3.experimental._

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_fp_exp2 ==
  *
  * === Abstract ===
  *
  * Floating Point Base-2 Exponential
  *
  * === Parameters ===
  *
  * | Parameter         | Legal Range           | Default | Description                                                                 |
  * |-------------------|-----------------------|---------|-----------------------------------------------------------------------------|
  * | sig_width         | 2 to 23 bits          |         | Word length of fraction field of floating-point numbers a and z.             |
  * | exp_width         | 4 to 8 bits           |         | Word length of biased exponent of floating point numbers a and z;            |
  * | ieee_compliance   | 0 or 1                |         | When 1, the generated architecture is fully compliant with IEEE 754 standard |
  * |                   |                       |         | including the use of denormals and NaNs.                                    |
  * | arch              | 0 or 1                |         |                                                                             |
  *
  * === Ports ===
  *
  * | Name    | Width                    | Direction | Description       |
  * |---------|--------------------------|-----------|-------------------|
  * | a       | sig_width+exp_width+1    | Input     | Input data        |
  * | status  | 8                        | Output    | Status flags      |
  * | z       | sig_width+exp_width+1    | Output    | exp2(a)           |
  */
class CW_fp_exp2(val sig_width: Int = 23, val exp_width: Int = 8, val ieee_compliance: Int = 1, val arch: Int = 0)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "ieee_compliance" -> ieee_compliance,
        "arch" -> arch
      )
    ) {
  // Validation of all parameters
  require(sig_width >= 2 && sig_width <= 23, "sig_width must be in range [2, 23]")
  require(exp_width >= 4 && exp_width <= 8, "exp_width must be in range [4, 8]")
  require(Set(0, 1, 3).contains(ieee_compliance), "ieee_compliance must be 0, 1, or 3")
  require(arch >= 0 && arch <= 1, "arch must be in range [0, 1]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt((sig_width + exp_width + 1).W))
  })
}
