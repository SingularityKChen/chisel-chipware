import chisel3._
import chisel3.experimental._

/**
  * == CW_lfsr_updn ==
  *
  * === Abstract ===
  *
  * LFSR Up/Down Counter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | 2 to 50      | 8            | Word length of counter |
  *
  * === Ports ===
  *
  * | Name   | Width      | Direction | Description                       |
  * |--------|------------|-----------|-----------------------------------|
  * | updn   | 1 bit      | Input     | Input high for count up and low for count down |
  * | cen    | 1 bit      | Input     | Input count enable                |
  * | clk    | 1 bit      | Input     | Clock                             |
  * | reset  | 1 bit      | Input     | Asynchronous reset, active low    |
  * | count  | width bit(s) | Output  | Output count bus                  |
  * | tercnt | 1 bit      | Output    | Output terminal count             |
  *
  * @param width  Word length of counter
  */
class CW_lfsr_updn(val width: Int = 8)
    extends BlackBox(
      Map(
        "width" -> width
      )
    ) {
  // Validation of all parameters
  require(width >= 2 && width <= 50, "width must be in the range [2, 50]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val updn:   Bool  = Input(Bool())
    val cen:    Bool  = Input(Bool())
    val clk:    Clock = Input(Clock())
    val reset:  Bool  = Input(Bool())
    val count:  UInt  = Output(UInt(width.W))
    val tercnt: Bool  = Output(Bool())
  })
}
