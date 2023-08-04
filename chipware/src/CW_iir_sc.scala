import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_iir_sc ==
  *
  * === Abstract ===
  *
  * High-Speed Digital IIR Filter with Static Coefficients
  *
  * === Parameters ===
  *
  * | Parameter          | Legal Range                                      | Default | Description                                                                 |
  * |--------------------|--------------------------------------------------|---------|-----------------------------------------------------------------------------|
  * | data_in_width      | >=2                                              | 4       | Input data word length                                                      |
  * | data_out_width     | >=2                                              | 6       | Width of output data                                                        |
  * | frac_data_out_width| 0 to data_out_width-1                            | 0       | Width of fraction portion of data_out                                       |
  * | feedback_width     | >=2                                              | 8       | Width of feedback_data (feedback_data is internal to the CW_iir_sc)         |
  * | max_coef_width     | 2 to 31                                          | 4       | Maximum coefficient word length                                             |
  * | frac_coef_width    | 0 to max_coef_width-1                            | 0       | Width of the fraction portion of the coefficients                            |
  * | saturation_mode    | 0 or 1                                           | 1       | Controls the mode of operation of the saturation output                     |
  * | out_reg            | 0 or 1                                           | 1       | Controls whether data_out and saturation are registered                      |
  * | A1_coef            | -2<sup>(max_coef_width-1)</sup> to 2<sup>(max_coef_width-1)</sup> -1 | -2      | Constant coefficient value A1                                                |
  * | A2_coef            | -2<sup>(max_coef_width-1)</sup> to 2<sup>(max_coef_width-1)</sup> -1 | 3       | Constant coefficient value A2                                                |
  * | B0_coef            | -2<sup>(max_coef_width-1)</sup> to 2<sup>(max_coef_width-1)</sup> -1 | 5       | Constant coefficient value B0                                                |
  * | B1_coef            | -2<sup>(max_coef_width-1)</sup> to 2<sup>(max_coef_width-1)</sup> -1 | -6      | Constant coefficient value B1                                                |
  * | B2_coef            | -2<sup>(max_coef_width-1)</sup> to 2<sup>(max_coef_width-1)</sup> -1 | -2      | Constant coefficient value B2                                                |
  *
  * === Ports ===
  *
  * | Name         | Width         | Direction | Description                                                 |
  * |--------------|---------------|-----------|-------------------------------------------------------------|
  * | clk          | 1 bit         | Input     | Clock signal. All internal registers are sensitive on the positive edge of clk and all setup and hold times are with respect to this edge of clk. |
  * | rst_n        | 1 bit         | Input     | Synchronous reset. Clears all registers                    |
  * | init_n       | 1 bit         | Input     | Synchronous signal to clear all registers                  |
  * | enable       | 1 bit         | Input     | Active-high signal to enable all registers                 |
  * | data_in      | data_in_width bit(s) | Input | Input data.                                                |
  * | data_out     | data_out_width bit(s) | Output | Accumulated sum of products of the IIR filter.            |
  * | saturation   | 1 bit         | Output    | Used to indicate the output data or feedback data is in saturation. |
  *
  * @param data_in_width        Input data word length
  * @param data_out_width       Width of output data
  * @param frac_data_out_width  Width of fraction portion of data_out
  * @param feedback_width       Width of feedback_data (internal to CW_iir_sc)
  * @param max_coef_width       Maximum coefficient word length
  * @param frac_coef_width      Width of the fraction portion of the coefficients
  * @param saturation_mode      Controls the mode of operation of the saturation output
  * @param out_reg              Controls whether data_out and saturation are registered
  * @param A1_coef              Constant coefficient value A1
  * @param A2_coef              Constant coefficient value A2
  * @param B0_coef              Constant coefficient value B0
  * @param B1_coef              Constant coefficient value B1
  * @param B2_coef              Constant coefficient value B2
  */
class CW_iir_sc(
  val data_in_width:       Int = 4,
  val data_out_width:      Int = 6,
  val frac_data_out_width: Int = 0,
  val feedback_width:      Int = 8,
  val max_coef_width:      Int = 4,
  val frac_coef_width:     Int = 0,
  val saturation_mode:     Int = 1,
  val out_reg:             Int = 1,
  val A1_coef:             Int = -2,
  val A2_coef:             Int = 3,
  val B0_coef:             Int = 5,
  val B1_coef:             Int = -6,
  val B2_coef:             Int = -2)
    extends BlackBox(
      Map(
        "data_in_width" -> data_in_width,
        "data_out_width" -> data_out_width,
        "frac_data_out_width" -> frac_data_out_width,
        "feedback_width" -> feedback_width,
        "max_coef_width" -> max_coef_width,
        "frac_coef_width" -> frac_coef_width,
        "saturation_mode" -> saturation_mode,
        "out_reg" -> out_reg,
        "A1_coef" -> A1_coef,
        "A2_coef" -> A2_coef,
        "B0_coef" -> B0_coef,
        "B1_coef" -> B1_coef,
        "B2_coef" -> B2_coef
      )
    )
    with HasBlackBoxPath {
  require(data_in_width >= 2, "data_in_width must be >= 2")
  require(data_out_width >= 2, "data_out_width must be >= 2")
  require(
    frac_data_out_width >= 0 && frac_data_out_width <= data_out_width - 1,
    "frac_data_out_width should be in range [0, data_out_width - 1]"
  )
  require(feedback_width >= 2, "feedback_width must be >= 2")
  require(max_coef_width >= 2 && max_coef_width <= 31, "max_coef_width should be in range [2, 31]")
  require(
    frac_coef_width >= 0 && frac_coef_width <= max_coef_width - 1,
    "frac_coef_width should be in range [0, max_coef_width - 1]"
  )
  require(saturation_mode == 0 || saturation_mode == 1, "saturation_mode must be 0 or 1")
  require(out_reg == 0 || out_reg == 1, "out_reg must be 0 or 1")

  val io = IO(new Bundle {
    val clk:        Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val init_n:     Bool  = Input(Bool())
    val enable:     Bool  = Input(Bool())
    val data_in:    UInt  = Input(UInt(data_in_width.W))
    val data_out:   UInt  = Output(UInt(data_out_width.W))
    val saturation: Bool  = Output(Bool())
  })
}
