import chisel3._
import chisel3.experimental._ // To enable experimental features

/**
  * == CW_fp_i2flt ==
  *
  * === Abstract ===
  *
  * Integer to Floating Point Converter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range     | Default | Description                                      |
  * |------------|-----------------|---------|--------------------------------------------------|
  * | sig_width  | 2 to 253 bits   | 23      | Word length of fraction field of floating-point number `z`   |
  * | exp_width  | 3 to 31 bits    | 8       | Word length of biased exponent of floating-point number `z`  |
  * | isize      | 3 to 512 bits   | 32      | Word length of integer number `a`                  |
  * | isign      | 0 or 1          | 1       | 0: unsigned integer number, 1: 2's compliment integer number |
  *
  * === Ports ===
  *
  * | Name     | Width                           | Direction | Description            |
  * |----------|---------------------------------|-----------|------------------------|
  * | a        | isize bits                      | Input     | Signed/Unsigned integer number |
  * | rnd      | 3 bits                          | Input     | Rounding mode          |
  * | status   | 8 bits                          | Output    | Status flags           |
  * | z        | sig_width+exp_width+1 bits      | Output    | Floating Point number  |
  *
  * @param sig_width Word length of fraction field of floating-point number `z`
  * @param exp_width Word length of biased exponent of floating-point number `z`
  * @param isize Word length of integer number `a`
  * @param isign 0: unsigned integer number, 1: 2's compliment integer number
  */
class CW_fp_i2flt(val sig_width: Int = 23, val exp_width: Int = 8, val isize: Int = 32, val isign: Int = 1)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "isize" -> isize,
        "isign" -> isign
      )
    ) {
  // Validation of all parameters
  require(sig_width >= 2 && sig_width <= 253, s"sig_width must be in the range [2, 253], but got $sig_width")
  require(isize >= 3 && isize <= 512, s"isize must be in the range [3, 512], but got $isize")
  require(exp_width >= 2 && exp_width <= 31, s"exp_width must be in the range [2, 31], but got $exp_width")
  require(isign >= 0 && isign <= 1, s"isign must be either 0 or 1, but got $isign")

  protected val bit_width_z: Int = sig_width + exp_width + 1

  val io = IO(new Bundle {
    val a:      UInt = Input(UInt(isize.W))
    val rnd:    UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt(bit_width_z.W))
  })
}
