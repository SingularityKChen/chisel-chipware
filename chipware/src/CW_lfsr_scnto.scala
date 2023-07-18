import chisel3._
import chisel3.experimental._

/**
  * == CW_lfsr_scnto ==
  *
  * === Abstract ===
  *
  * LFSR Counter with Static Count-to Flag
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | 2 to 50      | 8            | Word length of counter |
  * | count_to      | 1 to 2<sup>width</sup>-2 | 5          | count_to bus |
  *
  * === Ports ===
  *
  * | Name    | Width       | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | data   | width bit(s)| Input     | Input data |
  * | load   | 1 bit       | Input     | Input load, active low |
  * | cen    | 1 bit       | Input     | Input count enable |
  * | clk    | 1 bit       | Input     | Clock |
  * | reset  | 1           | Input     | Asynchronous reset, active low |
  * | count  | width bit(s)| Output    | Output count bus |
  * | tercnt | 1 bit       | Output    | Output terminal count |
  *
  * @param width  Width of counter (Range = 2 to 50)
  * @param count_to  Count to value
  */
class CW_lfsr_scnto(val width: Int = 8, val count_to: Int = 5)
    extends BlackBox(
      Map(
        "width" -> width,
        "count_to" -> count_to
      )
    ) {
  require(width >= 2 && width <= 50, "width must be in the range [2, 50]")
  require(count_to >= 1 && count_to <= (1 << (width - 2)), "count_to must be in the range [1, 2^width-2]")

  val io = IO(new Bundle {
    val data:   UInt  = Input(UInt(width.W))
    val load:   Bool  = Input(Bool())
    val cen:    Bool  = Input(Bool())
    val clk:    Clock = Input(Clock())
    val reset:  Bool  = Input(Bool())
    val count:  UInt  = Output(UInt(width.W))
    val tercnt: Bool  = Output(Bool())
  })
}
