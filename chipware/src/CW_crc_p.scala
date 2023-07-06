import chisel3._
import chisel3.experimental._

/**
  * == CW_crc_p ==
  *
  * === Abstract ===
  *
  * Universal Parallel (Combinational) CRC Generator/Checker
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | data_width    | 1 to 512     | 16           | Width of data_in |
  * | poly_size     | 2 to 64      | 16           | Size of the CRC polynomial and thus the width of crc_in and crc_out |
  * | crc_cfg       | 0 to 7       | 7            | CRC initialization and insertion configuration |
  * | bit_order     | 0 to 3       | 3            | Bit and byte order configuration |
  * | poly_coef0    | 1 to 65535   | 4129         | Polynomial coefficients 0 through 15 |
  * | poly_coef1    | 0 to 65535   | 0            | Polynomial coefficients 16 through 31 |
  * | poly_coef2    | 0 to 65535   | 0            | Polynomial coefficients 32 through 47 |
  * | poly_coef3    | 0 to 65535   | 0            | Polynomial coefficients 48 through 63 |
  *
  * === Ports ===
  *
  * | Name    | Width       | Direction | Description  |
  * |---------|-------------|-----------|--------------|
  * | data_in | data_width  | Input     | Input data used for both generating and checking for valid CRC |
  * | crc_in  | poly_size   | Input     | Input CRC value used to check a record (not used when generating CRC from data_in) |
  * | crc_ok  | 1           | Output    | Indicates a correct residual CRC value |
  * | crc_out | poly_size   | Output    | Provides the CRC check bits to be appended to the input data to form a valid record (data_in and crc_in) |
  *
  * @param data_width Width of data_in
  * @param poly_size Size of the CRC polynomial and thus the width of crc_in and crc_out
  * @param crc_cfg CRC initialization and insertion configuration
  * @param bit_order Bit and byte order configuration
  * @param poly_coef0 Polynomial coefficients 0 through 15
  * @param poly_coef1 Polynomial coefficients 16 through 31
  * @param poly_coef2 Polynomial coefficients 32 through 47
  * @param poly_coef3 Polynomial coefficients 48 through 63
  */
class CW_crc_p(
  val data_width: Int = 16,
  val poly_size:  Int = 16,
  val crc_cfg:    Int = 7,
  val bit_order:  Int = 3,
  val poly_coef0: Int = 4129,
  val poly_coef1: Int = 0,
  val poly_coef2: Int = 0,
  val poly_coef3: Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "poly_size" -> poly_size,
        "crc_cfg" -> crc_cfg,
        "bit_order" -> bit_order,
        "poly_coef0" -> poly_coef0,
        "poly_coef1" -> poly_coef1,
        "poly_coef2" -> poly_coef2,
        "poly_coef3" -> poly_coef3
      )
    ) {
  // Validation of all parameters
  require(data_width >= 1 && data_width <= 512, "data_width must be in range [1, 512]")
  require(poly_size >= 2 && poly_size <= 64, "poly_size must be in range [2, 64]")
  require(crc_cfg >= 0 && crc_cfg <= 7, "crc_cfg must be in range [0, 7]")
  require(bit_order >= 0 && bit_order <= 3, "bit_order must be in range [0, 3]")
  require(poly_coef0 >= 1 && poly_coef0 <= 65535, "poly_coef0 must be in range [1, 65535]")
  require(poly_coef1 >= 0 && poly_coef1 <= 65535, "poly_coef1 must be in range [0, 65535]")
  require(poly_coef2 >= 0 && poly_coef2 <= 65535, "poly_coef2 must be in range [0, 65535]")
  require(poly_coef3 >= 0 && poly_coef3 <= 65535, "poly_coef3 must be in range [0, 65535]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val data_in: UInt = Input(UInt(data_width.W))
    val crc_in:  UInt = Input(UInt(poly_size.W))
    val crc_ok:  Bool = Output(Bool())
    val crc_out: UInt = Output(UInt(poly_size.W))
  })
}
