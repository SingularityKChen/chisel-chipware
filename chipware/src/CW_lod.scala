import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_lod ==
  *
  * === Abstract ===
  *
  * Leading One’s Detector
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | a_width | >= 2 | 8 | Bit width of a |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | a | UInt(a_width.W) | Input | Input port |
  * | enc | UInt((log_a_width + 1).W) | Output | Number of leading 1’s in a before first 0. |
  * | dec | UInt(a_width.W) | Output | One hot decode of a |
  *
  * @param a_width  Bit width of a
  */
class CW_lod(val a_width: Int = 8) extends BlackBox(Map("a_width" -> a_width)) with HasBlackBoxPath {
  // Validation of parameters
  require(a_width >= 2, "a_width must be >= 2")
  protected val log_a_width: Int = log2Ceil(a_width)

  val io = IO(new Bundle {
    val a:   UInt = Input(UInt(a_width.W))
    val enc: UInt = Output(UInt((log_a_width + 1).W))
    val dec: UInt = Output(UInt(a_width.W))
  })
}
