import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_minmax ==
  *
  * === Abstract ===
  *
  * CW_minmax determines the minimum/maximum value of multiple inputs which are concatenated together in a single input bus.
  *
  * === Parameters ===
  *
  * | Parameter     | Legal Range | Default | Description                      |
  * |---------------|-------------|---------|----------------------------------|
  * | width         | >= 1        | 4       | Width of each segment of data    |
  * | num_inputs    | >= 2        | 2       | Number of data segments          |
  *
  * === Ports ===
  *
  * | Name    | Width                 | Direction | Description                      |
  * |---------|-----------------------|-----------|----------------------------------|
  * | a       | (num_inputs * width)  | Input     | Input bus                        |
  * | tc      | 1                     | Input     | Two's complement flag            |
  * | min_max | 1                     | Input     | Min/Max flag (0: min, 1: max)    |
  * | value   | width                 | Output    | Minimum/Maximum value            |
  * | index   | log2(num_inputs)      | Output    | Index of the min/max value       |
  *
  * @param width      Width of each segment of data
  * @param num_inputs Number of data segments
  */
class CW_minmax(val width: Int = 4, val num_inputs: Int = 2)
    extends BlackBox(
      Map(
        "width" -> width,
        "num_inputs" -> num_inputs
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 1, "width must be >= 1")
  require(num_inputs >= 2, "num_inputs must be >= 2")
  protected val log2_num_inputs: Int = log2Ceil(num_inputs)
  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:       UInt = Input(UInt((num_inputs * width).W))
    val tc:      Bool = Input(Bool())
    val min_max: Bool = Input(Bool())
    val value:   UInt = Output(UInt(width.W))
    val index:   UInt = Output(UInt(log2_num_inputs.W))
  })
}
