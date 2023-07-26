import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_inc ==
  *
  * === Abstract ===
  *
  * Incrementer
  *
  * This component increments integer input A producing the output Z.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | wA >= 1      | 8          | Bit width of A and Z |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | wA         | Input     | Integer input            |
  * | Z      | wA         | Output    | Integer output           |
  *
  * @param wA  Bit width of A and Z
  */
class CW_inc(val wA: Int = 8) extends BlackBox(Map("wA" -> wA)) with HasBlackBoxPath {
  // Validation of the parameter
  require(wA >= 1, "wA must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val Z: UInt = Output(UInt(wA.W))
  })
}
