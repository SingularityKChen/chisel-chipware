import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_norm ==
  *
  * === Abstract ===
  *
  * Normalization for Fractional Input
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | a_width       | >= 2         | 8            | Word size of a, b |
  * | srch_wind     | 2 to a_width | 8            | Search windows for leading 1-bit |
  * | exp_width     | >= 1         | 4            | Word size of exp_offset, exp_adj |
  * | exp_ctr       | 0 to 1       | 0            | Control of exp_adj |
  *
  * === Ports ===
  *
  * | Name       | Width         | Direction | Description  |
  * |------------|---------------|-----------|--------------|
  * | a          | a_width       | Input     | Input data   |
  * | exp_offset | exp_width     | Input     | Offset value |
  * | no_detect  | 1             | Output    | 0: bit found 1: bit not found |
  * | ovfl       | 1             | Output    | Output exp_adj is negative or incorrect |
  * | b          | a_width       | Output    | Normalized output |
  * | exp_adj    | exp_width     | Output    | exp_ctr=0: exp_offset + (left shift count) exp_ctr=1: exp_offset - (left shift count) |
  *
  * @param a_width     Word size of a, b
  * @param srch_wind   Search windows for leading 1-bit
  * @param exp_width   Word size of exp_offset, exp_adj
  * @param exp_ctr     Control of exp_adj
  */
class CW_norm(val a_width: Int = 8, val srch_wind: Int = 8, val exp_width: Int = 4, val exp_ctr: Int = 0)
    extends BlackBox(
      Map(
        "a_width" -> a_width,
        "srch_wind" -> srch_wind,
        "exp_width" -> exp_width,
        "exp_ctr" -> exp_ctr
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(a_width >= 2, "a_width must be >= 2")
  require(srch_wind >= 2 && srch_wind <= a_width, "srch_wind should be in range [2, a_width]")
  require(exp_width >= 1, "exp_width must be >= 1")
  require(exp_ctr >= 0 && exp_ctr <= 1, "exp_ctr should be in range [0, 1]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:          UInt = Input(UInt(a_width.W))
    val exp_offset: UInt = Input(UInt(exp_width.W))
    val no_detect:  Bool = Output(Bool())
    val ovfl:       Bool = Output(Bool())
    val b:          UInt = Output(UInt(a_width.W))
    val exp_adj:    UInt = Output(UInt(exp_width.W))
  })
}
