import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_lbsh ==
  *
  * === Abstract ===
  *
  * General Purpose Barrel Shifter with Preferred Left Direction for Shifting
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | A_width | >= 2 | 8 | Bit width of A and B |
  * | SH_width | >= 1, <= log2Ceil(A_width) | 3 | Bit width of SH |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A | A_width | Input | Input to be shifted. |
  * | SH | SH_width | Input | Shift control signal. |
  * | SH_TC | 1 bit | Input | Shift control 0 = unsigned 1 = signed |
  * | B | A_width | Output | Shifted output data. |
  *
  * @param A_width  Bit width of A and B
  * @param SH_width  Bit width of SH
  */
class CW_lbsh(val A_width: Int = 8, val SH_width: Int = 3)
    extends BlackBox(
      Map(
        "A_width" -> A_width,
        "SH_width" -> SH_width
      )
    )
    with HasBlackBoxPath {
  // Validation of parameters
  require(A_width >= 2, "A_width must be >= 2")
  require(SH_width >= 1 && SH_width <= log2Ceil(A_width), s"SH_width should be >= 1 and <= log2Ceil(A_width)")

  val io = IO(new Bundle {
    val A:     UInt = Input(UInt(A_width.W))
    val SH:    UInt = Input(UInt(SH_width.W))
    val SH_TC: Bool = Input(Bool())
    val B:     UInt = Output(UInt(A_width.W))
  })
}
