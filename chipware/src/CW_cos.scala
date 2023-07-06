import chisel3._
import chisel3.experimental._

/**
  * == CW_cos ==
  *
  * === Abstract ===
  *
  * Combinational Cosine
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | A_width       | 2 to 34      | 34           | A input width  |
  * | cos_width     | 2 to 66 (if A_width <= 10) or 2 to 34 (if A_width > 10 and A_width <= 34) | 34 | COS output width |
  *
  * === Ports ===
  *
  * | Name  | Width      | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | A_width    | Input     | A input                |
  * | COS    | cos_width  | Output    | COS output             |
  * @param A_width  A input width
  * @param cos_width  COS output width
  */
class CW_cos(val A_width: Int = 34, val cos_width: Int = 34)
    extends BlackBox(
      Map(
        "A_width" -> A_width,
        "cos_width" -> cos_width
      )
    ) {
  // Validation of all parameters
  require(A_width >= 2 && A_width <= 34, "A_width must be in range [2, 34]")
  require(cos_width >= 2, "cos_width must be >= 2")
  require(
    (A_width <= 10 && cos_width <= 66) || (A_width > 10 && A_width <= 34 && cos_width <= 34),
    "cos_width should be in range [2, 66] if A_width <= 10, or in range [2, 34] if A_width > 10 and A_width <= 34"
  )

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(A_width.W))
    val COS: UInt = Output(UInt(cos_width.W))
  })
}
