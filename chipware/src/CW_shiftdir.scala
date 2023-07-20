import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_shiftdir ==
  *
  * === Abstract ===
  *
  * Three Function Shifter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | wA >= 2      | 2          | A width and Z width |
  * | wSH           | wSH >= 1     | 1          | SH width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | wA         | Input     | Input data |
  * | SH     | wSH        | Input     | Shift amount |
  * | LEFT   | 1          | Input     | Direction control input |
  * | TC     | 1          | Input     | Control input that determines if the right shift is logical or arithmetic. |
  * | Z      | wA         | Output    | Output result |
  *
  * === Description ===
  *
  * | LEFT | TC | Operation | Equivalent Function |
  * |------|----|-----------|---------------------|
  * | 0 | 0 | logical right shift | CW_LSHIFTR |
  * | 0 | 1 | arithmetic right shift | CW_ASHIFTR |
  * | 1 | X | logical left shift | CW_LSHIFTL |
  *
  * @param wA  A width and Z width
  * @param wSH  SH width
  */
class CW_shiftdir(val wA: Int = 2, val wSH: Int = 1)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wSH" -> wSH
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 2, "wA must be >= 2")
  require(wSH >= 1, "wSH must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:    UInt = Input(UInt(wA.W))
    val SH:   UInt = Input(UInt(wSH.W))
    val LEFT: Bool = Input(Bool())
    val TC:   Bool = Input(Bool())
    val Z:    UInt = Output(UInt(wA.W))
  })
}
