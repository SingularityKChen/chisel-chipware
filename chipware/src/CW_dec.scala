import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_dec ==
  *
  * === Abstract ===
  *
  * Decrementer. Provides a basic decrementer. The CW_dec component decrements the A input, computing Z = A - 1.
  * The width of A and Z are determined by wA.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | wA >= 1      | 8          | A width and Z width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | wA         | Input     | A input                |
  * | Z      | wA         | Output    | Z output               |
  *
  * @param wA  A width and Z width
  */
class CW_dec(val wA: Int = 8) extends BlackBox(Map("wA" -> wA)) with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 1, "wA must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val Z: UInt = Output(UInt(wA.W))
  })
}
