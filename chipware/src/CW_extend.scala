import chisel3._
import chisel3.experimental._ // To enable experimental features
import chisel3.util._

/**
  * == CW_extend ==
  *
  * === Abstract ===
  *
  * Arithmetic Extension
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | wA >= 1      | 8          | A input width  |
  * | wZ            | wZ >= 1      | 8          | Z output width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | wA         | Input     | A input                |
  * | TC     | 1          | Input     | Control input. When the TC input is 0, A is treated as unsigned. When the TC input is 1, A is treated as signed. |
  * | Z      | wZ         | Output    | Z output               |
  * @param wA  A input width
  * @param wZ  Z output width
  */
class CW_extend(val wA: Int = 8, val wZ: Int = 8)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wZ" -> wZ
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 1, "wA must be >= 1")
  require(wZ >= 1, "wZ must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt(wZ.W))
  })
}
