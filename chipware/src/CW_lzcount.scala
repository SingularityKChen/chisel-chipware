import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_lzcount ==
  *
  * === Abstract ===
  *
  * Leading Zero Counter.
  * Determines the number of leading 0’s in the A input signal.
  * The result is provided as a binary number on the Z output.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA  | wA >= 2  | 2  | A input width  |
  * | wZ  | wZ >= 1  | 1  | Z output width  |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A  | wA  | Input  | Input data |
  * | Z  | wZ  | Output  | Number of leading zeroes in A |
  * | All0  | 1  | Output  | Indication that A=0 |
  *
  * @param wA  A input width
  * @param wZ  Z output width
  */
class CW_lzcount(val wA: Int = 2, val wZ: Int = 1)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wZ" -> wZ
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 2, "wA must be >= 2")
  require(wZ >= 1, "wZ must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:    UInt = Input(UInt(wA.W))
    val Z:    UInt = Output(UInt(wZ.W))
    val All0: Bool = Output(Bool())
  })
}
