// filename: CW_bc_7.scala
import chisel3._
import chisel3.util.HasBlackBoxPath

// ScalaDoc before the definition of the Chisel BlackBox class
/**
  * == CW_bc_7 ==
  *
  * === Abstract ===
  *
  * The BSDL description of this is of type BC_7 as described
  * in BSDL package STD_1149_1_1990.
  *
  * === Ports ===
  *
  * | Name        | Width | Direction | Function                                   |
  * |-------------|-------|-----------|--------------------------------------------|
  * | capture_clk | 1     | Input     | Clock for capturing the data                |
  * | update_clk  | 1     | Input     | Clock for the update stage                  |
  * | capture_en  | 1     | Input     | Active low, data enable for the capture stage |
  * | update_en   | 1     | Input     | Active high, data enable for the update stage |
  * | shift_dr    | 1     | Input     | Enables shifting of the data one stage towards the serial output of the BS chain |
  * | mode1       | 1     | Input     | Determines if data_out is controlled by BS cell or by output_data signal |
  * | mode2       | 1     | Input     | Determines if ic_input is controlled by BS cell or by pin_input signal |
  * | si          | 1     | Input     | Serial input coming from the previous BS cell |
  * | pin_input   | 1     | Input     | Input pin                                  |
  * | control_out | 1     | Input     | Control for output enable                   |
  * | output_data | 1     | Input     | Output logic signal                         |
  * | ic_input    | 1     | Output    | Input logic signal                          |
  * | data_out    | 1     | Output    | Output data                                |
  * | so          | 1     | Output    | Serial path out to the next BS cell         |
  */
class CW_bc_7 extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val capture_clk: Clock = Input(Clock())
    val update_clk:  Clock = Input(Clock())
    val capture_en:  Bool  = Input(Bool())
    val update_en:   Bool  = Input(Bool())
    val shift_dr:    Bool  = Input(Bool())
    val mode1:       Bool  = Input(Bool())
    val mode2:       Bool  = Input(Bool())
    val si:          Bool  = Input(Bool())
    val pin_input:   Bool  = Input(Bool())
    val control_out: Bool  = Input(Bool())
    val output_data: Bool  = Input(Bool())
    val ic_input:    Bool  = Output(Bool())
    val data_out:    Bool  = Output(Bool())
    val so:          Bool  = Output(Bool())
  })
}
