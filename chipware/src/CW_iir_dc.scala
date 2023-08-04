import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_iir_dc ==
  *
  * === Abstract ===
  *
  * High-Speed Digital IIR Filter with Dynamic Coefficients
  *
  * === Parameters ===
  *
  * | Parameter          | Legal Range                  | Default | Description |
  * |--------------------|------------------------------|---------|-------------|
  * | data_in_width      | >=2                          | 8       | Input data word length |
  * | data_out_width     | >=2                          | 16      | Width of output data |
  * | frac_data_out_width| 0 to data_out_width-1        | 4       | Width of fraction portion of data_out |
  * | feedback_width     | >=2                          | 12      | Width of feedback_data |
  * | max_coef_width     | >=2                          | 8       | Maximum coefficient word length |
  * | frac_coef_width    | 0 to max_coef_width-1        | 4       | Width of the fraction portion of the coefficients |
  * | saturation_mode    | 0 or 1                       | 0       | Controls the mode of operation of the saturation output |
  * | out_reg            | 0 or 1                       | 1       | Controls whether data_out and saturation are registered |
  *
  * === Ports ===
  *
  * | Name       | Width            | Direction | Description |
  * |------------|------------------|-----------|-------------|
  * | clk        | 1 bit            | In        | Clock signal |
  * | rst_n      | 1 bit            | In        | Asynchronous reset, active-low |
  * | init_n     | 1 bit            | In        | Synchronous, active-low signal to clear all registers |
  * | enable     | 1 bit            | In        | Active-high signal to enable all registers |
  * | A1_coef    | max_coef_width   | In        | Two's complement value of coefficient A1 |
  * | A2_coef    | max_coef_width   | In        | Two's complement value of coefficient A2 |
  * | B0_coef    | max_coef_width   | In        | Two's complement value of coefficient B0 |
  * | B1_coef    | max_coef_width   | In        | Two's complement value of coefficient B1 |
  * | B2_coef    | max_coef_width   | In        | Two's complement value of coefficient B2 |
  * | data_in    | data_in_width    | In        | Input data |
  * | data_out   | data_out_width   | Out       | Accumulated sum of products of the IIR filter |
  * | saturation | 1 bit            | Out       | Used to indicate the output data or feedback data is in saturation |
  *
  * @param data_in_width Input data word length
  * @param data_out_width Width of output data
  * @param frac_data_out_width Width of fraction portion of data_out
  * @param feedback_width Width of feedback_data
  * @param max_coef_width Maximum coefficient word length
  * @param frac_coef_width Width of the fraction portion of the coefficients
  * @param saturation_mode Controls the mode of operation of the saturation output
  * @param out_reg Controls whether data_out and saturation are registered
  */
class CW_iir_dc(
  val data_in_width:       Int = 8,
  val data_out_width:      Int = 16,
  val frac_data_out_width: Int = 4,
  val feedback_width:      Int = 12,
  val max_coef_width:      Int = 8,
  val frac_coef_width:     Int = 4,
  val saturation_mode:     Int = 0,
  val out_reg:             Int = 1)
    extends BlackBox(
      Map(
        "data_in_width" -> data_in_width,
        "data_out_width" -> data_out_width,
        "frac_data_out_width" -> frac_data_out_width,
        "feedback_width" -> feedback_width,
        "max_coef_width" -> max_coef_width,
        "frac_coef_width" -> frac_coef_width,
        "saturation_mode" -> saturation_mode,
        "out_reg" -> out_reg
      )
    ) {
  require(data_in_width >= 2, "data_in_width must be >= 2")
  require(data_out_width >= 2, "data_out_width must be >= 2")
  require(
    frac_data_out_width >= 0 && frac_data_out_width < data_out_width,
    "frac_data_out_width should be in range [0, data_out_width)"
  )
  require(feedback_width >= 2, "feedback_width must be >= 2")
  require(max_coef_width >= 2, "max_coef_width must be >= 2")
  require(
    frac_coef_width >= 0 && frac_coef_width < max_coef_width,
    "frac_coef_width should be in range [0, max_coef_width)"
  )

  val io = IO(new Bundle {
    val clk:        Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val init_n:     Bool  = Input(Bool())
    val enable:     Bool  = Input(Bool())
    val A1_coef:    SInt  = Input(SInt(max_coef_width.W))
    val A2_coef:    SInt  = Input(SInt(max_coef_width.W))
    val B0_coef:    SInt  = Input(SInt(max_coef_width.W))
    val B1_coef:    SInt  = Input(SInt(max_coef_width.W))
    val B2_coef:    SInt  = Input(SInt(max_coef_width.W))
    val data_in:    SInt  = Input(SInt(data_in_width.W))
    val data_out:   SInt  = Output(SInt(data_out_width.W))
    val saturation: Bool  = Output(Bool())
  })
}
