import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_square ==
  *
  * === Abstract ===
  *
  * Squarer
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | wA >= 2      | 8          | A input width |
  *
  * === Ports ===
  *
  * | Name  | Width       | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | wA         | Input     | Input data |
  * | TC     | 1          | Input     | Control input. <br> When the TC input is 0, A is treated as unsigned. <br> When the TC input is 1, A is treated as signed. |
  * | Z      | 2 * wA - 1 | Output    | Output square  |
  *
  * @param wA  A input width
  */
class CW_square(val wA: Int = 8) extends BlackBox(Map("wA" -> wA)) with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 2, "wA must be >= 2")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt((2 * wA - 1).W))
  })
}
