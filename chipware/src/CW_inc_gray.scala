import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_inc_gray ==
  *
  * === Abstract ===
  *
  * Gray Incrementer
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | >= 1         | 8          | Input word length |
  *
  * === Ports ===
  *
  * | Name  | Width        | Direction | Description           |
  * |-------|--------------|-----------|-----------------------|
  * | a     | width bit(s) | Input     | Gray coded input data |
  * | ci    | 1 bit        | Input     | Carry-in              |
  * | z     | width bit(s) | Output    | Gray coded output data|
  *
  * @param width  Input word length
  */
class CW_inc_gray(val width: Int = 8)
    extends BlackBox(
      Map(
        "width" -> width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 1, "width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:  UInt = Input(UInt(width.W))
    val ci: Bool = Input(Bool())
    val z:  UInt = Output(UInt(width.W))
  })
}
