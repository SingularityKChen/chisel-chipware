import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_par_gen ==
  *
  * === Abstract ===
  *
  * Parity Generator and Checker
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | 1 to 256     | 8            | Defines the width of the input bus |
  * | par_type      | 0 or 1       | 0            | Defines the type of parity |
  *
  * === Ports ===
  *
  * | Name    | Width       | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | datain | width bit(s)| Input     | Input data word to check or generate parity |
  * | parity | 1 bit       | Output    | Generated parity |
  *
  * @param width    Defines the width of the input bus
  * @param par_type Defines the type of parity
  */
class CW_par_gen(val width: Int = 8, val par_type: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "par_type" -> par_type
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 1 && width <= 256, "width must be in the range [1, 256]")
  require(par_type == 0 || par_type == 1, "par_type must be 0 or 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val datain: UInt = Input(UInt(width.W))
    val parity: UInt = Output(UInt(1.W))
  })
}
