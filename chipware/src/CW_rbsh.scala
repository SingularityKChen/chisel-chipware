import chisel3._
import chisel3.util._
import chisel3.experimental._

/**
  * == CW_rbsh ==
  *
  * === Abstract ===
  *
  * General Purpose Barrel Shifter with Preferred Right Direction for Shifting
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | A_width    | >= 2         | 8        | Width of input A and output B  |
  * | SH_width   | 1 to ceil(log2(A_width)) | 3 | Width of SH  |
  *
  * === Ports ===
  *
  * | Name       | Width        | Direction  | Description  |
  * |------------|--------------|------------|--------------|
  * | A          | A_width      | Input      | Input to be shifted.  |
  * | SH         | SH_width     | Input      | Shift control signal  |
  * | SH_TC      | -            | Input      | Shift control 0 = unsigned 1 = signed  |
  * | B          | A_width      | Output     | Shifted output data.  |
  *
  * @param A_width Width of input A and output B
  * @param SH_width Width of SH
  */
class CW_rbsh(val A_width: Int = 8, val SH_width: Int = 3)
    extends BlackBox(
      Map(
        "A_width" -> A_width,
        "SH_width" -> SH_width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(A_width >= 2, "A_width must be >= 2")
  require(SH_width >= 1 && SH_width < A_width, s"SH_width should be in range [1, ${A_width - 1}]")
  require(SH_width <= log2Ceil(A_width), s"SH_width should be <= ${log2Ceil(A_width)}")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:     UInt = Input(UInt(A_width.W))
    val SH:    UInt = Input(UInt(SH_width.W))
    val SH_TC: Bool = Input(Bool())
    val B:     UInt = Output(UInt(A_width.W))
  })
}
