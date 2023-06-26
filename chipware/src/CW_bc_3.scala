import chisel3._

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_bc_3 ==
  *
  * === Abstract ===
  *
  * The BSDL description of this is of type BC_3 as described in BSDL package STD_1149_1_1990.
  *
  * === Ports ===
  *
  * | Name         | Width | Direction | Description                                              |
  * |--------------|-------|-----------|----------------------------------------------------------|
  * | capture_clk  | 1     | input     | Clock for capturing the data                             |
  * | capture_en   | 1     | input     | Active low, data enable for the capture stage            |
  * | shift_dr     | 1     | input     | Enables shifting of the data one stage towards the serial output of the BS chain |
  * | mode         | 1     | input     | Decides if data_out is controlled by BS cell or data_in  |
  * | si           | 1     | input     | Serial input coming from the previous BS cell            |
  * | data_in      | 1     | input     | Data coming from the input pin                           |
  * | data_out     | 1     | output    | Output data                                              |
  * | so           | 1     | output    | Serial path out to the next BS cell                      |
  */
class CW_bc_3 extends BlackBox {
  val io = IO(new Bundle {
    // Define ports with type annotations
    val capture_clk: Clock = Input(Clock())
    val capture_en:  Bool  = Input(Bool())
    val shift_dr:    Bool  = Input(Bool())
    val mode:        Bool  = Input(Bool())
    val si:          Bool  = Input(Bool())
    val data_in:     Bool  = Input(Bool())
    val data_out:    Bool  = Output(Bool())
    val so:          Bool  = Output(Bool())
  })
}
