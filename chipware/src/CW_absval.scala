import chisel3._
import chisel3.experimental._

/**
  * == CW_absval ==
  *
  * === Abstract ===
  *
  * This module computes the absolute value of the input A.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | wA         | wA >= 1      | 8        | A and Z width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |-------|--------|-----------|--------------|
  * | A     | wA     | Input     | Input value  |
  * | Z     | wA     | Output    | Output absolute value |
  *
  * @param wA  A and Z width
  */
class CW_absval(val wA: Int = 8) extends BlackBox(Map("wA" -> wA)) {
  // Validation of the parameter
  require(wA >= 1, "wA must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val Z: UInt = Output(UInt(wA.W))
  })
}
