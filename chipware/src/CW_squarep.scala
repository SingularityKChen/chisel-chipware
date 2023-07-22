import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_squarep ==
  *
  * === Abstract ===
  *
  * Partial Product Square Function
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | width >= 1   | 8          | Width of multiplier a |
  *
  * === Ports ===
  *
  * | Name  | Width          | Direction | Description  |
  * |--------|----------------|-----------|------------------------|
  * | a      | width          | Input     | Multiplier |
  * | tc     | 1              | Input     | Control switch <br> tc = 0: Input/Output data is Unsigned <br> tc = 1: Input/Output data is Signed |
  * | out0   | width * 2      | Output    | Partial product of a x a |
  * | out1   | width * 2      | Output    | Partial product of a x a |
  *
  * @param width  Input width
  */
class CW_squarep(val width: Int = 8)
    extends BlackBox(
      Map(
        "width" -> width
      )
    )
    with HasBlackBoxPath {
  // Validation of the parameter
  require(width >= 1, "width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(width.W))
    val tc:   Bool = Input(Bool())
    val out0: UInt = Output(UInt((width * 2).W))
    val out1: UInt = Output(UInt((width * 2).W))
  })
}
