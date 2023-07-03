// Import required packages
import chisel3._
import chisel3.experimental._
import chisel3.util._

// Define the parameterized Chisel BlackBox class with ScalaDoc
/**
  * == CW_fifoctl_s1_sf ==
  *
  * === Abstract ===
  *
  * Synchronous (Single-Clock) FIFO Controller with Static Flags
  *
  * === Parameters ===
  *
  * | Parameter | Values        | Description                                                                                  |
  * |-----------|---------------|----------------------------------------------------------------------------------------------|
  * | depth     | 2 to 2<sup>24</sup>     | Number of memory elements used in FIFO (used to size the address ports) Default: 4           |
  * | ae_level  | 1 to depth-1  | Almost empty level (the number of words in the FIFO at or below which the almost_empty flag  |
  * |           |               | is active) Default: 1                                                                       |
  * | af_level  | 1 to depth-1  | Almost full level (the number of empty memory locations in the FIFO at which the this flag   |
  * |           |               | is active. Default: 1                                                                        |
  * | err_mode  | 0 to 2        | Error mode Default: 0                                                                        |
  * |           |               | 0 = underflow/overflow and pointer latched checking,                                         |
  * |           |               | 1 = underflow/overflow latched checking,                                                     |
  * |           |               | 2 = underflow/overflow unlatched checking                                                    |
  * | rst_mode  | 0 or 1        | Reset mode Default: 0                                                                        |
  * |           |               | 0 = asynchronous reset,                                                                      |
  * |           |               | 1 = synchronous reset                                                                        |
  *
  * === Ports ===
  *
  * | Name          | Width              | Direction | Description                                     |
  * |---------------|--------------------|-----------|-------------------------------------------------|
  * | clk           | 1 bit              | Input     | Input clock                                     |
  * | rst_n         | 1 bit              | Input     | Reset input, active low asynchronous if rst_mode |
  * |               |                    |           | = 0, synchronous if rst_mode = 1                |
  * | push_req_n    | 1 bit              | Input     | FIFO push request, active low                    |
  * | pop_req_n     | 1 bit              | Input     | FIFO pop request, active low                     |
  * | diag_n        | 1 bit              | Input     | Diagnostic control                              |
  * | we_n          | 1 bit              | Output    | Write enable output for write port of RAM,       |
  * |               |                    |           | active low                                      |
  * | empty         | 1 bit              | Output    | FIFO empty output, active high                   |
  * | almost_empty  | 1 bit              | Output    | FIFO almost empty output, asserted when FIFO     |
  * |               |                    |           | level <= ae_level, active high                   |
  * | half_full     | 1 bit              | Output    | FIFO half full output, active high               |
  * | almost_full   | 1 bit              | Output    | FIFO almost full output, asserted when FIFO      |
  * |               |                    |           | level >= (depth - af_level), active high         |
  * | full          | 1 bit              | Output    | FIFO full output, active high                    |
  * | error         | 1 bit              | Output    | FIFO error output, active high                   |
  * | wr_addr       | ceil(log2[depth])  | Output    | Address output to write port of RAM              |
  * | rd_addr       | ceil(log2[depth])  | Output    | Address output to read port of RAM               |
  *
  * @param depth    Number of memory elements used in FIFO (used to size the address ports)
  * @param ae_level Almost empty level (the number of words in the FIFO at or below which the almost_empty flag is active)
  * @param af_level Almost full level (the number of empty memory locations in the FIFO at which the this flag is active)
  * @param err_mode Error mode
  * @param rst_mode Reset mode
  */
class CW_fifoctl_s1_sf(
  val depth:    Int = 4,
  val ae_level: Int = 1,
  val af_level: Int = 1,
  val err_mode: Int = 0,
  val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "depth" -> depth,
        "ae_level" -> ae_level,
        "af_level" -> af_level,
        "err_mode" -> err_mode,
        "rst_mode" -> rst_mode
      )
    ) {
  // Validation of all parameters
  require(depth >= 2 && depth <= math.pow(2, 24), "depth must be between 2 and 2^24")
  require(ae_level >= 1 && ae_level <= depth - 1, "ae_level must be between 1 and depth - 1")
  require(af_level >= 1 && af_level <= depth - 1, "af_level must be between 1 and depth - 1")
  require(err_mode >= 0 && err_mode <= 2, "err_mode must be between 0 and 2")
  require(rst_mode >= 0 && rst_mode <= 1, "rst_mode must be either 0 or 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:          Clock = Input(Clock())
    val rst_n:        Bool  = Input(Bool())
    val push_req_n:   Bool  = Input(Bool())
    val pop_req_n:    Bool  = Input(Bool())
    val diag_n:       Bool  = Input(Bool())
    val we_n:         Bool  = Output(Bool())
    val empty:        Bool  = Output(Bool())
    val almost_empty: Bool  = Output(Bool())
    val half_full:    Bool  = Output(Bool())
    val almost_full:  Bool  = Output(Bool())
    val full:         Bool  = Output(Bool())
    val error:        Bool  = Output(Bool())
    val wr_addr:      UInt  = Output(UInt(log2Ceil(depth).W))
    val rd_addr:      UInt  = Output(UInt(log2Ceil(depth).W))
  })
}
