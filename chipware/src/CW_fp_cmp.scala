import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_fp_cmp ==
  *
  * === Abstract ===
  *
  * Floating-Point Comparator
  *
  * === Parameters ===
  *
  * | Parameter        | Legal Range   | Default      | Description    |
  * |------------------|---------------|--------------|----------------|
  * | sig_width        | 2 to 253 bits |              | Word length of fraction field of floating-point numbers a, b, z0, and z1. |
  * | exp_width        | 3 to 31 bits  |              | Word length of biased exponent of floating-point numbers a, b, z0, and z1. |
  * | ieee_compliance  | 0 or 1        |              | When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of denormals and NaNs. |
  * | quieten_nans     | 0 or 1        |              | Quiet NaNs. |
  *
  * === Ports ===
  *
  * | Name        | Width                          | Direction | Description                                           |
  * |-------------|--------------------------------|-----------|-------------------------------------------------------|
  * | a           | sig_width + exp_width + 1 bits | Input     | Input data                                            |
  * | b           | sig_width + exp_width + 1 bits | Input     | Input data                                            |
  * | status0     | 8 bits                         | Output    | Status flag corresponding to z0                       |
  * | status1     | 8 bits                         | Output    | Status flag corresponding to z1                       |
  * | z0          | sig_width + exp_width + 1 bits | Output    | min(a, b) when zctr = 0, else max(a, b)                |
  * | z1          | sig_width + exp_width + 1 bits | Output    | max(a, b) when zctr = 0, else min(a, b)                |
  * | zctr        | 1 bit                          | Input     | Determines value passed to z0 and z1                   |
  * | agtb        | 1 bit                          | Output    | a > b                                                 |
  * | altb        | 1 bit                          | Output    | a < b                                                 |
  * | aeqb        | 1 bit                          | Output    | a = b                                                 |
  * | unordered   | 1 bit                          | Output    | When input NaN and ieee_compliance = 1                |
  *
  * @param sig_width        Word length of fraction field of floating-point numbers a, b, z0, and z1
  * @param exp_width        Word length of biased exponent of floating-point numbers a, b, z0, and z1
  * @param ieee_compliance  When 1, the generated architecture is fully compliant with IEEE 754 standard including the use of denormals and NaNs
  * @param quieten_nans     Quiet NaNs
  */
class CW_fp_cmp(
  val sig_width:       Int = 23,
  val exp_width:       Int = 8,
  val ieee_compliance: Int = 1,
  val quieten_nans:    Int = 1)
    extends BlackBox(
      Map(
        "sig_width" -> sig_width,
        "exp_width" -> exp_width,
        "ieee_compliance" -> ieee_compliance,
        "quieten_nans" -> quieten_nans
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(sig_width >= 2 && sig_width <= 253, s"sig_width must be between 2 and 253 (inclusive), but got $sig_width")
  require(exp_width >= 3 && exp_width <= 31, s"exp_width must be between 3 and 31 (inclusive), but got $exp_width")
  require(
    List(0, 1, 3).contains(ieee_compliance),
    s"ieee_compliance must be either 0, 1, or 3, but got $ieee_compliance"
  )
  require(List(0, 1).contains(quieten_nans), s"quieten_nans must be either 0 or 1, but got $quieten_nans")

  val io = IO(new Bundle {
    val a:         UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b:         UInt = Input(UInt((sig_width + exp_width + 1).W))
    val status0:   UInt = Output(UInt(8.W))
    val status1:   UInt = Output(UInt(8.W))
    val z0:        UInt = Output(UInt((sig_width + exp_width + 1).W))
    val z1:        UInt = Output(UInt((sig_width + exp_width + 1).W))
    val zctr:      Bool = Input(Bool())
    val agtb:      Bool = Output(Bool())
    val altb:      Bool = Output(Bool())
    val aeqb:      Bool = Output(Bool())
    val unordered: Bool = Output(Bool())
  })
}
