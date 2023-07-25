import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_decode_en ==
  *
  * === Abstract ===
  *
  * Binary Decoder with Enable.
  * CW_decode_en decodes binary address of "width" bits to 2<sup>width</sup> bits output line
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | >= 1         | 3            | input 'a' bit width. width of decoded out 'b' is  2<sup>width</sup> |
  *
  * === Ports ===
  *
  * | Name  | Width      | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | en     | 1          | Input     | enable                  |
  * | a      | width      | Input     | binary address          |
  * | b      | 2<sup>width</sup>    | Output    | decoded output          |
  *
  * @param width  input 'a' bit width and width of decoded out 'b' is 2<sup>width</sup>
  */
class CW_decode_en(val width: Int = 3)
    extends BlackBox(
      Map(
        "width" -> width
      )
    )
    with HasBlackBoxPath {
  // Validation of the width parameter
  require(width >= 1, "width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val en: Bool = Input(Bool())
    val a:  UInt = Input(UInt(width.W))
    val b:  UInt = Output(UInt((1 << width).W))
  })
}
