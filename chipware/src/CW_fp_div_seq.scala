import chisel3._
import chisel3.experimental._

/**
 * == CW_fp_div_seq ==
 *
 * === Abstract ===
 *
 * CW_fp_div_seq is a floating-point sequential divider component that divides two floating point numbers, a and b,
 * producing floating-point outputs z and status according to the rounding mode rnd.
 *
 * === Parameters ===
 *
 * | Parameter         | Legal Range        | Default | Description                                                                   |
 * |-------------------|--------------------|---------|-------------------------------------------------------------------------------|
 * | sig_width         | 2 to 256 bits      | 23      | Fraction field length of a, b, z                                              |
 * | exp_width         | 3 to 31 bits       | 8       | Biased exponent length of a, b, z                                             |
 * | ieee_compliance   | 0 to 1             | 0       | 0: honors neither Denormals nor NaNs, 1: honors Denormals & partially honors NaNs |
 * | num_cyc           | >3 & <= 2*sig_width+3 | 5       | Number of clock cycles to produce a valid product value                        |
 * | rst_mode          | 0 or 1             | 0       | Reset Mode: 0 = Asynchronous reset, 1 = Synchronous reset                     |
 * | input_mode        | 0 or 1             | 1       | Indicates type of input: 0 = Registered input, 1 = Non-registered input       |
 * | output_mode       | 0 or 1             | 1       | Indicates type of output: 0 = Registered output, 1 = Non-registered output    |
 * | early_start       | 0 or 1             | 0       | Indicates start of processing: 0 - computation starts in the second cycle, 1 - computation starts in the first cycle |
 * | internal_reg      | 0 or 1             | 1       | Internal register enables the pipeline operation                               |
 * | arch              | 0 or 1             | 0       | 0: Implementation based on Classic division algorithm, 1: Implementation based on SRT algorithm |
 *
 * === Ports ===
 *
 * | Name        | Width                          | Direction | Description                          |
 * |-------------|--------------------------------|-----------|--------------------------------------|
 * | clk         | 1 bit                          | Input     | Clock                                |
 * | rst_n       | 1 bit                          | Input     | Reset                                |
 * | start       | 1 bit                          | Input     | Start operation                      |
 * | rnd         | 1 bit                          | Input     | Rounding Mode                        |
 * | a           | sig_width + exp_width + 1 bits | Input     | Input Data                           |
 * | b           | sig_width + exp_width + 1 bits | Input     | Input Data                           |
 * | z           | sig_width + exp_width + 1 bits | Output    | Result                               |
 * | status      | 8 bits                         | Output    | Status flag                          |
 * | complete    | 1 bit                          | Output    | 0: Division is not yet complete, 1: Division is complete and outputs z and status are ready |
 *
 * @param sig_width         Fraction field length of a, b, z
 * @param exp_width         Biased exponent length of a, b, z
 * @param ieee_compliance   0: honors neither Denormals nor NaNs, 1: honors Denormals & partially honors NaNs
 * @param num_cyc           Number of clock cycles to produce a valid product value
 * @param rst_mode          Reset Mode: 0 = Asynchronous reset, 1 = Synchronous reset
 * @param input_mode        Indicates type of input: 0 = Registered input, 1 = Non-registered input
 * @param output_mode       Indicates type of output: 0 = Registered output, 1 = Non-registered output
 * @param early_start       Indicates start of processing: 0 - computation starts in the second cycle, 1 - computation starts in the first cycle
 * @param internal_reg      Internal register enables the pipeline operation: 0: internal register is absent, 1: internal register is present
 * @param arch              0: Implementation based on Classic division algorithm, 1: Implementation based on SRT algorithm
 */
class CW_fp_div_seq(val sig_width: Int = 23,
                    val exp_width: Int = 8,
                    val ieee_compliance: Int = 0,
                    val rst_mode: Int = 0,
                    val input_mode: Int = 1,
                    val output_mode: Int = 1,
                    val early_start: Int = 0,
                    val internal_reg: Int = 1,
                    val num_cyc: Int = 5,
                    val arch: Int = 0) extends BlackBox(Map(
  "sig_width" -> sig_width,
  "exp_width" -> exp_width,
  "ieee_compliance" -> ieee_compliance,
  "rst_mode" -> rst_mode,
  "input_mode" -> input_mode,
  "output_mode" -> output_mode,
  "early_start" -> early_start,
  "internal_reg" -> internal_reg,
  "num_cyc" -> num_cyc,
  "arch" -> arch
)) {
  // Validation of all parameters
  require(sig_width >= 2 && sig_width <= 256, "sig_width must be between 2 and 256")
  require(exp_width >= 3 && exp_width <= 31, "exp_width must be between 3 and 31")
  require(ieee_compliance >= 0 && ieee_compliance <= 1, "ieee_compliance must be either 0 or 1")
  require(rst_mode >= 0 && rst_mode <= 1, "rst_mode must be either 0 or 1")
  require(input_mode >= 0 && input_mode <= 1, "input_mode must be either 0 or 1")
  require(output_mode >= 0 && output_mode <= 1, "output_mode must be either 0 or 1")
  require(early_start >= 0 && early_start <= 1, "early_start must be either 0 or 1")
  require(internal_reg >= 0 && internal_reg <= 1, "internal_reg must be either 0 or 1")
  require(num_cyc > 3 && num_cyc <= (2 * sig_width + 3), "num_cyc must be greater than 3 and less than or equal to 2 * sig_width + 3")
  require(arch >= 0 && arch <= 1, "arch must be either 0 or 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk: Clock = Input(Clock())
    val rst_n: Bool = Input(Bool())
    val start: Bool = Input(Bool())
    val rnd: Bool = Input(Bool())
    val a: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val z: UInt = Output(UInt((sig_width + exp_width + 1).W))
    val status: UInt = Output(UInt(8.W))
    val complete: Bool = Output(Bool())
  })
}
