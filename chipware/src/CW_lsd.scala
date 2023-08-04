import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_lsd ==
  *
  * === Abstract ===
  *
  * Leading Signs Detector with encoded output
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | a_width    | >= 1         | 8        | Width of a   |
  *
  * === Ports ===
  *
  * | Name  | Width            | Direction | Description  |
  * |-------|------------------|-----------|--------------|
  * | a     | a_width          | Input     | Input        |
  * | dec   | a_width          | Output    | Decoded Value of A |
  * | enc   | ceil(log2(a_width)) | Output | Number of leading sign bits in a before the least significant sign bit |
  *
  * @param a_width  Width of a
  */
class CW_lsd(val a_width: Int = 8)
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
    val enc: UInt = Output(UInt(log_a_width.W))
    val dec: UInt = Output(UInt(a_width.W))
  })
}
