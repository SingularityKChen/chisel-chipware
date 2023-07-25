import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_decode ==
  *
  * === Abstract ===
  *
  * Decoder. Decodes the binary encoded input A to the one-hot output Z.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | wA >= 1      | 4            | A input width  |
  *
  * === Ports ===
  *
  * | Name  | Width       | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | wA         | Input     | Input value            |
  * | Z      | 2<sup>width</sup>  | Output    | Decoded output value   |
  *
  * @param wA  A input width
  */
class CW_decode(val wA: Int = 4) extends BlackBox(Map("wA" -> wA)) with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 1, "wA must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val Z: UInt = Output(UInt((1 << wA).W))
  })
}
