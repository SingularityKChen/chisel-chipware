import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_sla ==
  *
  * === Abstract ===
  *
  * Arithmetic Shifter with Preferred Left Direction
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | A_width       | A_width >= 2 | 4          | Bit width of A and B |
  * | SH_width      | SH_width >= 1 | 2          | Bit width of SH |
  *
  * === Ports ===
  *
  * | Name  | Width       | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | A_width    | Input     | Input data to be shifted |
  * | SH     | SH_width   | Input     | Shift control signal. It controls the number of shifting to be done. |
  * | SH_TC  | 1          | Input     | Shift control: <br> SH_TC=0, SH is unsigned <br> SH_TC=1, SH is signed |
  * | B      | A_width    | Output    | Shifted output data |
  *
  * @param A_width  Bit width of A and B
  * @param SH_width  Bit width of SH
  */
class CW_sla(val A_width: Int = 4, val SH_width: Int = 2)
    extends BlackBox(
      Map(
        "A_width" -> A_width,
        "SH_width" -> SH_width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(A_width >= 2, "A_width must be >= 2")
  require(SH_width >= 1, "SH_width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:     UInt = Input(UInt(A_width.W))
    val SH:    UInt = Input(UInt(SH_width.W))
    val SH_TC: Bool = Input(Bool())
    val B:     UInt = Output(UInt(A_width.W))
  })
}
