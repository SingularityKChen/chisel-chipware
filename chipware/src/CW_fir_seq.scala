import chisel3._
import chisel3.experimental._

/**
  * == CW_fir_seq ==
  *
  * === Abstract ===
  *
  * Sequential Digital FIR Filter Processor
  *
  * === Parameters ===
  *
  * | Parameter       | Legal Range  | Default      | Description               |
  * |-----------------|--------------|--------------|---------------------------|
  * | data_in_width   | >=1          | 8            | Input data word length    |
  * | coef_width      | >=1          | 8            | Coefficient word length   |
  * | data_out_width  | >=1          | 16           | Accumulator word length   |
  * | order           | 2 to 256     | 6            | FIR filter order          |
  * | addr_width      | Derived      | -            | Address width for internal use |
  *
  * === Ports ===
  *
  * | Name           | Width                 | Direction | Description                     |
  * |----------------|-----------------------|-----------|---------------------------------|
  * | clk            | 1                     | Input     | Input clock                     |
  * | rst_n          | 1                     | Input     | Asynchronous reset, active low  |
  * | coef_shift_en  | 1                     | Input     | Enable coef_shift loading at coef_in |
  * | tc             | 1                     | Input     | Defines data_in and coef_in as two's complement or unsigned |
  * | run            | 1                     | Input     | Handshake signal that initiates the processing of data sample on data_in port |
  * | data_in        | data_in_width         | Input     | Input data                      |
  * | coef_in        | coef_width            | Input     | Input coefficient               |
  * | init_acc_val   | data_out_width        | Input     | Initial accumulated sum value   |
  * | start          | 1                     | Output    | Handshake signal generated by synchronizing the run input with clock |
  * | hold           | 1                     | Output    | Handshake signal that indicates the completion of processing of current data_in sample and filter is ready to process the next sample |
  * | data_out       | data_out_width        | Output    | Accumulated sum of products of the FIR filter |
  *
  * @param data_in_width  Input data word length
  * @param coef_width     Coefficient word length
  * @param data_out_width Accumulator word length
  * @param order          FIR filter order
  */
class CW_fir_seq(val data_in_width: Int = 8, val coef_width: Int = 8, val data_out_width: Int = 16, val order: Int = 6)
    extends BlackBox(
      Map(
        "data_in_width" -> data_in_width,
        "coef_width" -> coef_width,
        "data_out_width" -> data_out_width,
        "order" -> order
      )
    ) {
  require(data_in_width >= 1, "data_in_width must be >= 1")
  require(coef_width >= 1, "coef_width must be >= 1")
  require(data_out_width >= 1, "data_out_width must be >= 1")
  require(order >= 2 && order <= 256, "order must be between 2 and 256")

  val io = IO(new Bundle {
    val clk:           Clock = Input(Clock())
    val rst_n:         Bool  = Input(Bool())
    val coef_shift_en: Bool  = Input(Bool())
    val tc:            Bool  = Input(Bool())
    val run:           Bool  = Input(Bool())
    val data_in:       UInt  = Input(UInt(data_in_width.W))
    val coef_in:       UInt  = Input(UInt(coef_width.W))
    val init_acc_val:  UInt  = Input(UInt(data_out_width.W))
    val start:         Bool  = Output(Bool())
    val hold:          Bool  = Output(Bool())
    val data_out:      UInt  = Output(UInt(data_out_width.W))
  })
}
