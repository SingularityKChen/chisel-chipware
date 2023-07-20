import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_fp_ln ==
  *
  * === Abstract ===
  * Floating Point Natural Logarithm
  *
  * === Parameters ===
  *
  * | Parameter       | Legal Range  | Default | Description                                                         |
  * |-----------------|--------------|---------|---------------------------------------------------------------------|
  * | sig_width       | 2 to 112     | 23      | Word length of fraction field of floating-point numbers a and z.    |
  * | exp_width       | 3 to 31      | 8       | Word length of biased exponent of floating-point numbers a and z.   |
  * | ieee_compliance | 0 or 1       | 1       | When 1, the generated architecture is fully compliant with IEEE 754  |
  * |                 |              |         | standard including the use of denormals and NaNs.                   |
  * | arch            |              | 0       |                                                                     |
  * | extra_prec      |              | 0       |                                                                     |
  *
  * === Ports ===
  *
  * | Name    | Width                        | Direction | Description  |
  * |---------|------------------------------|-----------|--------------|
  * | a       | sig_width+exp_width+1 bits  | Input     | Input data   |
  * | status  | 8 bits                       | Output    | Status flags |
  * | z       | sig_width+exp_width+1 bits  | Output    | ln(a)        |
  *
  * @param sig_width       Word length of fraction field of floating-point numbers a and z.
  * @param exp_width       Word length of biased exponent of floating-point numbers a and z.
  * @param ieee_compliance When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of denormals and NaNs.
  * @param arch            -
  * @param extra_prec      -
  */
class CW_fp_ln(
  val sig_width:       Int = 23,
  val exp_width:       Int = 8,
  val ieee_compliance: Int = 1,
  val arch:            Int = 0,
  val extra_prec:      Int = 0)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "ieee_compliance" -> ieee_compliance,
        "arch" -> arch,
        "extra_prec" -> extra_prec
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(sig_width >= 2 && sig_width <= 112, "sig_width must be in the range [2, 112]")
  require(exp_width >= 3 && exp_width <= 31, "exp_width must be in the range [3, 31]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt((sig_width + exp_width + 1).W))
  })
}
