import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_pricod ==
  *
  * === Abstract ===
  *
  * Priority Coder
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | a_width    | >= 1         | 8        | Width of a   |
  *
  * === Ports ===
  *
  * | Name  | Width       | Direction | Description                  |
  * |-------|-------------|-----------|------------------------------|
  * | a     | a_width     | Input     | Input                        |
  * | cod   | a_width     | Output    | One-hot coded version of a   |
  * | zero  | 1           | Output    | All zero                     |
  *
  * @param a_width  Width of a
  */
class CW_pricod(val a_width: Int = 8)
    extends BlackBox(
      Map(
        "a_width" -> a_width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(a_width >= 1, "a_width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(a_width.W))
    val cod:  UInt = Output(UInt(a_width.W))
    val zero: Bool = Output(Bool())
  })
}
