// filename: CW_bc_8.scala
import chisel3._
import chisel3.util.HasBlackBoxPath

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_bc_8 ==
  *
  * === Abstract ===
  *
  * The BSDL description of this is of type BC_8 as described in BSDL package STD_1149_1_1990.
  *
  * === Ports ===
  *
  * | Name        | Width | Direction | Description                                          |
  * |-------------|-------|-----------|------------------------------------------------------|
  * | capture_clk | 1     | input     | Clock for capturing the data                          |
  * | update_clk  | 1     | input     | Clock for the update stage                            |
  * | capture_en  | 1     | input     | Active low, data enable for the capture stage         |
  * | update_en   | 1     | input     | Active high, data enable for the update stage         |
  * | shift_dr    | 1     | input     | Enables shifting of the data one stage towards        |
  * |             |       |           | the serial output of the BS chain                     |
  * | mode        | 1     | input     | Determines if data_out is controlled by BS cell       |
  * |             |       |           | or by output_data signal                              |
  * | si          | 1     | input     | Serial input coming from the previous BS cell         |
  * | pin_input   | 1     | input     | Input pin                                            |
  * | output_data | 1     | input     | Output logic signal                                  |
  * | ic_input    | 1     | output    | Input logic signal                                   |
  * | data_out    | 1     | output    | Output data                                          |
  * | so          | 1     | output    | Serial path out to the next BS cell                   |
  */
class CW_bc_8 extends BlackBox with HasBlackBoxPath {
  // Define ports with type annotations
  val io = IO(new Bundle {
    val capture_clk: Clock = Input(Clock())
    val update_clk:  Clock = Input(Clock())
    val capture_en:  Bool  = Input(Bool())
    val update_en:   Bool  = Input(Bool())
    val shift_dr:    Bool  = Input(Bool())
    val mode:        Bool  = Input(Bool())
    val si:          Bool  = Input(Bool())
    val pin_input:   Bool  = Input(Bool())
    val output_data: Bool  = Input(Bool())
    val ic_input:    Bool  = Output(Bool())
    val data_out:    Bool  = Output(Bool())
    val so:          Bool  = Output(Bool())
  })
}
