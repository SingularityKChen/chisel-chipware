import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_compge ==
  *
  * === Abstract ===
  *
  * Two Function Comparator
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | wA >= 1      | 8            | A and B input width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | wA         | Input     | A input                |
  * | B      | wA         | Input     | B input                |
  * | TC     | 1          | Input     | 0: A and B are interpreted as unsigned integers 1: A and B are interpreted as signed integers |
  * | AGB    | 1          | Output    | High when A > B        |
  * | AEB    | 1          | Output    | High when A = B        |
  * @param wA  A and B input width
  */
class CW_compge(val wA: Int = 8) extends BlackBox(Map("wA" -> wA)) with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 1, "wA must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(wA.W))
    val B:   UInt = Input(UInt(wA.W))
    val TC:  Bool = Input(Bool())
    val AGB: Bool = Output(Bool())
    val AEB: Bool = Output(Bool())
  })
}
