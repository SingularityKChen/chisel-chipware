import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_thermdec ==
  *
  * === Abstract ===
  *
  * Decoder
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | 1 to 16      | 3            | Bit width of input a |
  *
  * === Ports ===
  *
  * | Name  | Width        | Direction | Description  |
  * |--------|--------------|-----------|------------------------|
  * | en     | 1            | Input     | Enable input (active high) |
  * | a      | width        | Input     | Binary Input |
  * | b      | 2<sup>width</sup> | Output    | Decode output  |
  * @param width  Bit width of input a
  */
class CW_thermdec(val width: Int = 3)
    extends BlackBox(
      Map(
        "width" -> width
      )
    )
    with HasBlackBoxPath {
  // Validation of width parameter
  require(width >= 1 && width <= 16, "width must be in the range [1, 16]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val en: Bool = Input(Bool())
    val a:  UInt = Input(UInt(width.W))
    val b:  UInt = Output(UInt((1 << width).W))
  })
}
