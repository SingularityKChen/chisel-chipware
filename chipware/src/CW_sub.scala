import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_sub ==
  *
  * === Abstract ===
  *
  * Subtracter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA | >= 1 | 8 | Bit width of A, B, and Z |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A | wA | Input | Integer input |
  * | B | wA | Input | Integer input |
  * | CI | 1 | Input | Carry in |
  * | Z | wA | Output | Integer output |
  * | CO | 1 | Output | Carry out |
  *
  * @param wA  Bit width of A, B, and Z
  */
class CW_sub(val wA: Int = 8)
    extends BlackBox(
      Map(
        "wA" -> wA
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 1, "wA must be >= 1")
  protected val bit_width_wA: Int = wA
  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wA.W))
    val CI: Bool = Input(Bool())
    val Z:  UInt = Output(UInt(wA.W))
    val CO: Bool = Output(Bool())
  })
}
