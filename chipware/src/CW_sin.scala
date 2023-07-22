import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_sin ==
  *
  * === Abstract ===
  *
  * Combinatorial Sine
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | A_width       | 2 to 34      | 34           | Bit width of A |
  * | sin_width     | 2 to 66      | 34           | Bit width of SIN |
  *
  * === Ports ===
  *
  * | Name  | Width      | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | A_width    | Input     | Angle  |
  * | SIN    | sin_width  | Output    | Sine data output |
  * @param A_width  Bit width of A
  * @param sin_width  Bit width of SIN
  */
class CW_sin(val A_width: Int = 34, val sin_width: Int = 34)
    extends BlackBox(
      Map(
        "A_width" -> A_width,
        "sin_width" -> sin_width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(A_width >= 2 && A_width <= 34, "A_width must be in range [2, 34]")
  require(sin_width >= 2, "sin_width must be >= 2")
  if (A_width <= 10) {
    require(sin_width <= 66, "sin_width must be <= 66 when A_width <= 10")
  } else {
    require(sin_width <= 34, "sin_width must be <= 34 when A_width > 10")
  }

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(A_width.W))
    val SIN: UInt = Output(UInt(sin_width.W))
  })
}
