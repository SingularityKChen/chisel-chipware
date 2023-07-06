import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_csa ==
  *
  * === Abstract ===
  *
  * ...
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | width >= 1   | 4            | Bit width of the inputs and outputs |
  *
  * === Ports ===
  *
  * | Name   | Width       | Direction | Description  |
  * |--------|-------------|-----------|------------------------|
  * | a      | width       | Input     | Input a                |
  * | b      | width       | Input     | Input b                |
  * | c      | width       | Input     | Input c                |
  * | ci     | 1           | Input     | Carry-in               |
  * | carry  | width       | Output    | Carry output           |
  * | sum    | width       | Output    | Sum output             |
  * | co     | 1           | Output    | Carry-out              |
  *
  * @param width  Bit width of the inputs and outputs
  */
class CW_csa(val width: Int = 4) extends BlackBox(Map("width" -> width)) {
  // Validation of the width parameter
  require(width >= 1, "width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:     UInt = Input(UInt(width.W))
    val b:     UInt = Input(UInt(width.W))
    val c:     UInt = Input(UInt(width.W))
    val ci:    Bool = Input(Bool())
    val carry: UInt = Output(UInt(width.W))
    val sum:   UInt = Output(UInt(width.W))
    val co:    Bool = Output(Bool())
  })
}
