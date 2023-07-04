import chisel3._
import chisel3.experimental._

/**
  * == CW_bsh ==
  *
  * === Abstract ===
  *
  * A general purpose barrel shifter performs the rotate left function.
  * Data that is shifted past the most significant bit (MSB) wraps around to the
  * least significant bit (LSB).
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | A_width    | N/A          | N/A      | Width of input A |
  * | SH_width   | N/A          | N/A      | Width of input SH |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |-------|--------|-----------|--------------|
  * | A     | A_width  | Input     | Input to be shifted. |
  * | SH    | SH_width | Input     | Shift control signal |
  * | B     | A_width  | Output    | Shifted output data. |
  *
  * @param A_width Width of input A
  * @param SH_width Width of input SH
  */
class CW_bsh(val A_width: Int = 8, val SH_width: Int = 3)
    extends BlackBox(
      Map(
        "A_width" -> A_width,
        "SH_width" -> SH_width
      )
    ) {
  // Validation of all parameters
  require(A_width >= 2, "A_width must be >= 2")
  require(SH_width >= 1, "SH_width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(A_width.W))
    val SH: UInt = Input(UInt(SH_width.W))
    val B:  UInt = Output(UInt(A_width.W))
  })
}
