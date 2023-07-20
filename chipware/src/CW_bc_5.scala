// filename: CW_bc_5.scala
import chisel3._
import chisel3.util.HasBlackBoxPath

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_bc_5 ==
  *
  * === Abstract ===
  *
  * The BSDL description of this is of type BC_5 as described
  * in BSDL package STD_1149_1_1990.
  *
  * === Ports ===
  *
  * | Name        | Width | Direction | Function                                            |
  * |-------------|-------|-----------|-----------------------------------------------------|
  * | capture_clk | 1     | input     | Clock for capturing the data                         |
  * | update_clk  | 1     | input     | Clock for the update stage                           |
  * | capture_en  | 1     | input     | Active low, data enable for the capture stage        |
  * | update_en   | 1     | input     | Active high, data enable for the update stage        |
  * | shift_dr    | 1     | input     | Enables shifting of the data one stage towards       |
  * |             |       |           | the serial output of the BS chain                    |
  * | mode        | 1     | input     | Decides if data_out is controlled by BS cell or      |
  * |             |       |           | data_in                                             |
  * | intest      | 1     | input     | INTEST instruction                                  |
  * | si          | 1     | input     | Serial input coming from the previous BS cell        |
  * | data_in     | 1     | input     | Data coming from the input pin                       |
  * | data_out    | 1     | output    | Output data                                         |
  * | so          | 1     | output    | Serial path out to the next BS cell                  |
  */
class CW_bc_5 extends BlackBox with HasBlackBoxPath {
  // Define ports with type annotations
  val io = IO(new Bundle {
    val capture_clk: Clock = Input(Clock())
    val update_clk:  Clock = Input(Clock())
    val capture_en:  Bool  = Input(Bool())
    val update_en:   Bool  = Input(Bool())
    val shift_dr:    Bool  = Input(Bool())
    val mode:        Bool  = Input(Bool())
    val intest:      Bool  = Input(Bool())
    val si:          Bool  = Input(Bool())
    val data_in:     Bool  = Input(Bool())
    val data_out:    Bool  = Output(Bool())
    val so:          Bool  = Output(Bool())
  })
}
