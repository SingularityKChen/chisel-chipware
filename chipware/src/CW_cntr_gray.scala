import chisel3._
import chisel3.experimental._

/**
  * == CW_cntr_gray ==
  *
  * === Abstract ===
  *
  * Gray Code Counter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | >= 1         | 4          | Word length of counter |
  *
  * === Ports ===
  *
  * | Name    | Width       | Direction | Description                       |
  * |---------|-------------|-----------|-----------------------------------|
  * | clk     | 1 bit       | Input     | Clock                             |
  * | rst_n   | 1 bit       | Input     | Reset, asynchronous, active low   |
  * | init_n  | 1 bit       | Input     | Reset, synchronous, active low    |
  * | load_n  | 1 bit       | Input     | Enable data load to counter, active low |
  * | data    | width bit(s)| Input     | Counter load input                |
  * | cen     | 1 bit       | Input     | Count enable, active high         |
  * | count   | width bit(s)| Output    | Gray coded counter output         |
  *
  * @param width  Word length of counter
  */
class CW_cntr_gray(val width: Int = 4)
    extends BlackBox(
      Map(
        "width" -> width
      )
    ) {
  // Validation of all parameters
  require(width >= 1, "width must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:    Clock = Input(Clock())
    val rst_n:  Bool  = Input(Bool())
    val init_n: Bool  = Input(Bool())
    val load_n: Bool  = Input(Bool())
    val data:   UInt  = Input(UInt(width.W))
    val cen:    Bool  = Input(Bool())
    val count:  UInt  = Output(UInt(width.W))
  })
}
