import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_mac ==
  *
  * === Abstract ===
  *
  * Multiplier Accumulator. MAC = A * B + C
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | A_width       | A_width >= 1 | 8          | Width of A  |
  * | B_width       | B_width >= 1 | 8          | Width of B |
  *
  * === Ports ===
  *
  * | Name  | Width            | Direction | Description  |
  * |--------|------------------|-----------|------------------------|
  * | TC     | 1                | Input     | Signed/Unsigned control: 0=unsigned 1=signed |
  * | A      | A_width          | Input     | Multiplier input    |
  * | B      | B_width          | Input     | Multiplicand input  |
  * | C      | A_width + B_width| Input     | Addend              |
  * | MAC    | A_width + B_width| Output    | Output (A*B+C)      |
  * @param A_width  Width of A
  * @param B_width  Width of B
  */
class CW_mac(val A_width: Int = 8, val B_width: Int = 8)
    extends BlackBox(
      Map(
        "A_width" -> A_width,
        "B_width" -> B_width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(A_width >= 1, "A_width must be >= 1")
  require(B_width >= 1, "B_width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val TC:  Bool = Input(Bool())
    val A:   UInt = Input(UInt(A_width.W))
    val B:   UInt = Input(UInt(B_width.W))
    val C:   UInt = Input(UInt((A_width + B_width).W))
    val MAC: UInt = Output(UInt((A_width + B_width).W))
  })
}
