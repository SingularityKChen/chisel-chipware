import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_lzd ==
  *
  * === Abstract ===
  *
  * Leading Zeroes Detector.
  * Computes the count of leading zeros in the input argument and outputs this count as an unsigned binary number.
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Range  | Default  | Description  |
  * |--------------|--------------|----------|--------------|
  * | a_width      | a_width >= 1 | 8        | Input string width |
  *
  * === Ports ===
  *
  * | Name  | Width         | Direction | Description  |
  * |-------|---------------|-----------|--------------|
  * | a     | a_width       | Input     | Input port |
  * | enc   | ceil(log2[a_width]+1) | Output    | Number of leading zeroes in a before the first one. |
  * | dec   | a_width       | Output    | One hot decode of a |
  *
  * @param a_width  Input string width
  */
class CW_lzd(val a_width: Int = 8)
    extends BlackBox(
      Map(
        "a_width" -> a_width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(a_width >= 1, "a_width must be >= 1")
  protected val log_a_width: Int = log2Ceil(a_width)
  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:   UInt = Input(UInt(a_width.W))
    val enc: UInt = Output(UInt((log_a_width + 1).W))
    val dec: UInt = Output(UInt(a_width.W))
  })
}
