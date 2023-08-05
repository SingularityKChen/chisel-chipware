import chisel3._
import chisel3.util._
import chisel3.experimental._

/**
  * == CW_reg_s_pl ==
  *
  * === Abstract ===
  *
  * Register with Synchronous Reset and Load Enable
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Range    | Default      | Description                              |
  * |--------------|----------------|--------------|------------------------------------------|
  * | width        | >=1            | 8           | Width of d and q |
  * | reset_value  | If width <= 31, the value of reset_value must be between 0 and 2<sup>width</sup>-1. If width >= 32, the value of reset_value must be 0. | 0            | Reset value  |
  *
  * === Ports ===
  *
  * | Name         | Width          | Direction    | Description                     |
  * |--------------|----------------|--------------|---------------------------------|
  * | d            | width          | Input        | Input data bus                  |
  * | clk          | 1              | Input        | Clock input                     |
  * | reset_N      | 1              | Input        | Synchronous reset, active low   |
  * | enable       | 1              | Input        | Synchronous enable, active high |
  * | q            | width          | Output       | Output data bus                 |
  *
  * @param width       Width of d and q
  * @param reset_value Reset value
  */
class CW_reg_s_pl(val width: Int, val reset_value: Int)
    extends BlackBox(
      Map(
        "width" -> width,
        "reset_value" -> reset_value
      )
    )
    with HasBlackBoxPath {

  // Validate parameters
  require(width >= 1, "width must be >= 1")
  if (width <= 31) {
    require(reset_value >= 0 && reset_value < (1 << width), s"reset_value should be in range [0, ${1 << width})")
  } else {
    require(reset_value == 0, "reset_value should be == 0")
  }

  // Define ports with type annotations
  val io = IO(new Bundle {
    val d:       UInt  = Input(UInt(width.W))
    val clk:     Clock = Input(Clock())
    val reset_N: Bool  = Input(Bool())
    val enable:  Bool  = Input(Bool())
    val q:       UInt  = Output(UInt(width.W))
  })
}
