import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_lfsr_dcnto ==
  *
  * === Abstract ===
  *
  * LFSR Counter with Dynamic Count-to Flag
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | 1 to 50      | 8            | Word length of counter |
  *
  * === Ports ===
  *
  * | Name     | Width        | Direction | Description  |
  * |----------|--------------|-----------|------------------------|
  * | data     | width bit(s) | Input     | Input data             |
  * | count_to | width bit(s) | Input     | Input count_to_bus     |
  * | load     | 1 bit        | Input     | Input load data to counter, active low |
  * | cen      | 1 bit        | Input     | Input count enable     |
  * | clk      | 1 bit        | Input     | Clock                  |
  * | reset    | 1 bit        | Input     | Asynchronous reset, active low |
  * | count    | width bit(s) | Output    | Output count bus       |
  * | tercnt   | 1 bit        | Output    | Output terminal count  |
  *
  * @param width  Word length of counter
  */
class CW_lfsr_dcnto(val width: Int = 8)
    extends BlackBox(
      Map(
        "width" -> width
      )
    ) {
  // Validation of all parameters
  require(width >= 1 && width <= 50, "width must be in the range [1, 50]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val data:     UInt  = Input(UInt(width.W))
    val count_to: UInt  = Input(UInt(width.W))
    val load:     Bool  = Input(Bool())
    val cen:      Bool  = Input(Bool())
    val clk:      Clock = Input(Clock())
    val reset:    Bool  = Input(Bool())
    val count:    UInt  = Output(UInt(width.W))
    val tercnt:   Bool  = Output(Bool())
  })
}
