import chisel3._
import chisel3.experimental._ // To enable experimental features

/**
 * Adds the operand a and b to produce the sum in the number of clock cycles specified by the num_cyc parameter.
 * Addition is initiated when start is asserted active high for 1 clk period. Output bus sum is valid when complete is asserted.
 * If hold is asserted, then sum is held constant. Signal hold does not prevent a new sequence initiated by start.
 *
 * Signal rst_n is synchronous for a CW_add_seq instantiation when the parameter rst_mode = 1.
 * Sequential library cells with synchronous resets will be selected during mapping.
 * Signal rst_n is asynchronous for CW_add_seq instantiation when parameter rst_mode = 0.
 * Sequential cells with active low asynchronous reset will be used during synthesis.
 *
 * When the parameter input_mode = 0, the input data is not registered.
 * When input_mode = 1, the input data is registered.
 *
 * When the parameter output_mode = 0, the output data is not registered.
 * When output_mode = 1, the output data is registered.
 *
 * When the parameter early_start = 1, addition starts immediately after setting the start signal active high.
 * When early_start = 0, it starts with one clock cycle delay after the start signal is asserted.
 *
 * Within the first num_cyc clock cycles immediately after reset conditions are released (rst_n =1),
 * no start is initiated (that is, start remains 0) until the first assertion of complete (that is, complete =1).
 * This first complete =1 following the reset might yield invalid results and should be disregarded.
 *
 * @param a_width      Word size of input a
 * @param b_width      Word size of input b
 * @param num_cyc      Number of clock cycles to calculate the sum
 * @param rst_mode     Reset mode, 0 for asynchronous and 1 for synchronous reset
 * @param input_mode   Input mode, 0 for unregistered input and 1 for registered input
 * @param output_mode  Output mode, 0 for unregistered output and 1 for registered output
 * @param early_start  Early start mode, 0 for one clock cycle delay and 1 for immediate start after setting start signal high
 */
class CW_add_seq(val a_width: Int = 3,
                 val b_width: Int = 3,
                 val num_cyc: Int = 3,
                 val rst_mode: Int = 0,
                 val input_mode: Int = 1,
                 val output_mode: Int = 1,
                 val early_start: Int = 0) extends BlackBox(
  Map(
    "a_width" -> a_width,
    "b_width" -> b_width,
    "num_cyc" -> num_cyc,
    "rst_mode" -> rst_mode,
    "input_mode" -> input_mode,
    "output_mode" -> output_mode,
    "early_start" -> early_start
)) {
  val io = IO(new Bundle {
    val clk: Clock = Input(Clock())
    val rst_n: Bool = Input(Bool())
    val hold: Bool = Input(Bool())
    val start: Bool = Input(Bool())
    val a: UInt = Input(UInt(a_width.W))
    val b: UInt = Input(UInt(b_width.W))
    val complete: Bool = Output(Bool())
    val sum: UInt = Output(UInt(a_width.W))
  })
}
