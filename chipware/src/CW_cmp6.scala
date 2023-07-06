import chisel3._
import chisel3.experimental._

/**
  * == CW_cmp6 ==
  *
  * === Abstract ===
  *
  * Six Function Comparator
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
  * | TC     | 1          | Input     | TC input               |
  * | LT     | 1          | Output    | LT output              |
  * | GT     | 1          | Output    | GT output              |
  * | EQ     | 1          | Output    | EQ output              |
  * | LE     | 1          | Output    | LE output              |
  * | GE     | 1          | Output    | GE output              |
  * | NE     | 1          | Output    | NE output              |
  *
  * @param wA  A and B input width
  */
class CW_cmp6(val wA: Int = 8) extends BlackBox(Map("wA" -> wA)) {
  // Validation of wA parameter
  require(wA >= 1, "wA must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wA.W))
    val TC: Bool = Input(Bool())
    val LT: Bool = Output(Bool())
    val GT: Bool = Output(Bool())
    val EQ: Bool = Output(Bool())
    val LE: Bool = Output(Bool())
    val GE: Bool = Output(Bool())
    val NE: Bool = Output(Bool())
  })
}
