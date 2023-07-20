import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_fp_flt2i ==
  *
  * === Abstract ===
  *
  * Floating Point to Integer Converter
  *
  * === Parameters ===
  *
  * | Parameter         | Legal Range           | Default | Description                                                                 |
  * |-------------------|-----------------------|---------|-----------------------------------------------------------------------------|
  * | sig_width         | 2 to 253 bits         |         | Word length of fraction field of floating-point number a.                    |
  * | exp_width         | 3 to 31 bits          |         | Word length of biased exponent of floating point number a.                   |
  * | isize             | 3 to 512 bits         |         | Word length of integer number z.                                             |
  * | ieee_compliance   | 0 or 1                |         | When 1, the generated architecture recognizes denormal Infinity and NaNs.    |
  *
  * === Ports ===
  *
  * | Name    | Width                    | Direction | Description       |
  * |---------|--------------------------|-----------|-------------------|
  * | a       | sig_width+exp_width+1    | Input     | Floating Number   |
  * | rnd     | 3                        | Input     | Rounding mode     |
  * | status  | 8                        | Output    | Status flags      |
  * | z       | isize                    | Output    | Two's complement integer number |
  */
class CW_fp_flt2i(val sig_width: Int = 23, val exp_width: Int = 8, val isize: Int = 32, val ieee_compliance: Int = 1)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "isize" -> isize,
        "ieee_compliance" -> ieee_compliance
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(sig_width >= 2 && sig_width <= 253, "sig_width must be in range [2, 253]")
  require(exp_width >= 3 && exp_width <= 31, "exp_width must be in range [3, 31]")
  require(isize >= 3 && isize <= 512, "isize must be in range [3, 512]")
  require(Set(0, 1, 3).contains(ieee_compliance), "ieee_compliance must be 0, 1, or 3")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd:    UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt(isize.W))
  })
}
