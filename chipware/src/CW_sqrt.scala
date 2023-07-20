import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_sqrt ==
  *
  * === Abstract ===
  *
  * Square Root: Implementation - Newton-Raphson algorithm
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width | >= 2 | 8 | Input a word length |
  * | tc_mode | 0 or 1 | 0 | 0: a is interpreted as unsigned integer 1: a is interpreted as signed integer |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | a | width | Input | Integer radicand |
  * | root | ((width+1)/2)-1 | Output | Integer square root |
  *
  * @param width  Input a word length
  * @param tc_mode  0: a is interpreted as unsigned integer 1: a is interpreted as signed integer
  */
class CW_sqrt(val width: Int = 8, val tc_mode: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "tc_mode" -> tc_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 2, "width must be >= 2")
  require(tc_mode == 0 || tc_mode == 1, "tc_mode should be 0 or 1")
  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(width.W))
    val root: UInt = Output(UInt(((width + 1) / 2).W))
  })
}
