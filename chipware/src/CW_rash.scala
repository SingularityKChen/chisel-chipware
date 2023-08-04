import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_rash ==
  *
  * === Abstract ===
  *
  * Arithmetic Shifter with Preferred Right Direction
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | wA         | ≥ 2           | 4        | Bit width of A and Z  |
  * | wSH        | ≥ 1          | 2        | Bit width of SH  |
  *
  * === Ports ===
  *
  * | Name       | Width        | Direction  | Description            |
  * |------------|--------------|------------|------------------------|
  * | A          | wA           | Input      | Integer input            |
  * | DATA_TC    | 1            | Input      | 0: A is interpreted as unsigned integer 1: A is interpreted as signed integer            |
  * | SH         | wSH          | Input      | Shift control signal. It controls the number of shifting to be done.            |
  * | SH_TC      | 1            | Input      | Shift control 0: Unsigned shift 1: Signed shift            |
  * | Z          | wA           | Output     | Shifted integer output            |
  *
  * @param wA  Bit width of A and Z
  * @param wSH Bit width of SH
  */
class CW_rash(val wA: Int = 4, val wSH: Int = 2)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wSH" -> wSH
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 2, "wA must be >= 2")
  require(wSH >= 1, "wSH must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:       UInt = Input(UInt(wA.W))
    val DATA_TC: Bool = Input(Bool())
    val SH:      UInt = Input(UInt(wSH.W))
    val SH_TC:   Bool = Input(Bool())
    val Z:       UInt = Output(UInt(wA.W))
  })
}
