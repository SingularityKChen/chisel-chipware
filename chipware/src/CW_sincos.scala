import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_sincos ==
  *
  * === Abstract ===
  *
  * Combinatorial Sine or Cosine
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | A_width       | 2 to 34      | 34           | Bit width of A  |
  * | WAVE_width    | 2 to 66 (A_width <= 10), 2 to 34 (A_width > 10) | 34 | Bit width of WAVE |
  *
  * === Ports ===
  *
  * | Name    | Width      | Direction | Description  |
  * |---------|------------|-----------|--------------|
  * | A       | A_width    | Input     | Angle |
  * | SIN_COS | 1          | Input     | Sine (SIN_COS = 0) or cosine (SIN_COS = 1) |
  * | WAVE    | WAVE_width | Output    | Sine or cosine data output  |
  *
  * @param A_width    Bit width of A
  * @param WAVE_width Bit width of WAVE
  */
class CW_sincos(val A_width: Int = 34, val WAVE_width: Int = 34)
    extends BlackBox(
      Map(
        "A_width" -> A_width,
        "WAVE_width" -> WAVE_width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(A_width >= 2 && A_width <= 34, "A_width must be in range [2, 34]")
  require(WAVE_width >= 2, "WAVE_width must be >= 2")
  if (A_width <= 10) {
    require(WAVE_width <= 66, "WAVE_width must be <= 66 when A_width <= 10")
  } else {
    require(WAVE_width <= 34, "WAVE_width must be <= 34 when A_width > 10")
  }

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:       UInt = Input(UInt(A_width.W))
    val SIN_COS: Bool = Input(Bool())
    val WAVE:    UInt = Output(UInt(WAVE_width.W))
  })
}
