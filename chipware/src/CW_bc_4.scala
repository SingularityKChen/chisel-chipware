import chisel3._
import chisel3.util.HasBlackBoxPath

// Define the Chisel BlackBox class with ScalaDoc
/**
  * == CW_bc_4 ==
  *
  * === Abstract ===
  *
  * The BSDL description of this is of type BC_4 as described
  * in BSDL package STD_1149_1_1990.
  *
  * === Ports ===
  *
  * | Name         | Width | Direction | Description                                         |
  * |--------------|-------|-----------|-----------------------------------------------------|
  * | capture_clk  | 1     | input     | Clock for capturing the data                         |
  * | capture_en   | 1     | input     | Active low, data enable for the capture stage        |
  * | shift_dr     | 1     | input     | Enables shifting of the data one stage towards       |
  * |              |       |           | the serial output of the BS chain                     |
  * | si           | 1     | input     | Serial input coming from the previous BS cell         |
  * | data_in      | 1     | input     | Data coming from the input pin                        |
  * | so           | 1     | output    | Serial path out to the next BS cell                   |
  * | data_out     | 1     | output    | Output data                                          |
  */
class CW_bc_4 extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val capture_clk: Clock = Input(Clock())
    val capture_en:  Bool  = Input(Bool())
    val shift_dr:    Bool  = Input(Bool())
    val si:          Bool  = Input(Bool())
    val data_in:     Bool  = Input(Bool())
    val so:          Bool  = Output(Bool())
    val data_out:    Bool  = Output(Bool())
  })
}
