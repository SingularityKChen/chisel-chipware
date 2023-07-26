import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_incdec ==
  *
  * === Abstract ===
  *
  * Incrementer-Decrementer.
  *
  * Basic incrementer or decrementer functionality controlled by DEC.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | wA >= 1      | 8          | A and Z width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | wA         | Input     | Input data            |
  * | DEC    | 1          | Input     | Increment or decrement control input |
  * | Z      | wA         | Output    | Output result         |
  *
  * @param wA  A and Z width
  */
class CW_incdec(val wA: Int = 8) extends BlackBox(Map("wA" -> wA)) with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 1, "wA must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(wA.W))
    val DEC: Bool = Input(Bool())
    val Z:   UInt = Output(UInt(wA.W))
  })
}
