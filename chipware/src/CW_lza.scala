import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_lza ==
  *
  * === Abstract ===
  *
  * Leading Zeros Anticipator.
  * CW_lza will predict the number of leading zeros in between unsigned inputs a and b which are respectively the minuend and subtrahend terms in a difference.
  *
  * === Parameters ===
  *
  * | Parameter       | Legal Range | Default | Description          |
  * |-----------------|-------------|---------|----------------------|
  * | width           | 2 to 28 | 4       | Width of input arguments a and b |
  *
  * === Ports ===
  *
  * | Name   | Width         | Direction | Description |
  * |--------|---------------|-----------|-------------|
  * | a      | width         | Input     | Unsigned Minuend |
  * | b      | width         | Input     | Unsigned Subtrahend |
  * | count  | BWBOinputWidth| Output    | Anticipated number of leading zeros |
  *
  * @param width          Width of input arguments a and b
  */
class CW_lza(val width: Int = 4)
    extends BlackBox(
      Map(
        "width" -> width
      )
    )
    with HasBlackBoxPath {
  require(width >= 2 && width <= 28, "width must be in 2 to 28")
  protected val BWBOinputWidth: Int = log2Ceil(width)

  val io = IO(new Bundle {
    val a:     UInt = Input(UInt(width.W))
    val b:     UInt = Input(UInt(width.W))
    val count: UInt = Output(UInt(BWBOinputWidth.W))
  })
}
