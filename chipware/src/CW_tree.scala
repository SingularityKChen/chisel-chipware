import chisel3._
import chisel3.experimental._ // To enable experimental features
import chisel3.util._

/**
  * == CW_tree ==
  *
  * === Abstract ===
  *
  * CW_tree is a Wallace tree compressor built from 3:2 compressor cells (full adders).
  *
  * === Parameters ===
  *
  * | Parameter   | Legal Range | Default | Description                 |
  * |-------------|-------------|---------|-----------------------------|
  * | num_inputs  | >= 1        | 8       | Number of inputs            |
  * | input_width | >= 1        | 8       | Bit width of OUT0 and OUT1  |
  *
  * === Ports ===
  *
  * | Name  | Width                 | Direction | Description  |
  * |-------|-----------------------|-----------|--------------|
  * | INPUT | num_inputs*input_width | Input     | Input vector |
  * | OUT0  | input_width           | Output    | Partial sum  |
  * | OUT1  | input_width           | Output    | Partial sum  |
  *
  * @param num_inputs  Number of inputs
  * @param input_width Bit width of OUT0 and OUT1
  */
class CW_tree(val num_inputs: Int = 8, val input_width: Int = 8)
    extends BlackBox(
      Map(
        "num_inputs" -> num_inputs,
        "input_width" -> input_width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(num_inputs >= 1, "num_inputs must be >= 1")
  require(input_width >= 1, "input_width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val INPUT: UInt = Input(UInt((num_inputs * input_width).W))
    val OUT0:  UInt = Output(UInt(input_width.W))
    val OUT1:  UInt = Output(UInt(input_width.W))
  })
}
