import chisel3._
import chisel3.experimental._

/**
  * == CW_multadd ==
  *
  * === Abstract ===
  *
  * Multiplier-Adder
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | >= 1         | 8            | Width of A input |
  * | wB            | >= 1         | 8            | Width of B input |
  * | wC            | >= 1         | 16          | Width of C input |
  * | wZ            | >= 1         | 16          | Width of Z output |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A        | wA            | Input       | A input |
  * | B        | wB            | Input       | B input |
  * | C        | wC            | Input       | C input |
  * | TC      | 1              | Input       | Test Control |
  * | Z        | wZ            | Output     | Z output |
  *
  * @param wA Width of A input
  * @param wB Width of B input
  * @param wC Width of C input
  * @param wZ Width of Z output
  */
class CW_multadd(val wA: Int = 8, val wB: Int = 8, val wC: Int = 16, val wZ: Int = 16)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wB" -> wB,
        "wC" -> wC,
        "wZ" -> wZ
      )
    ) {
  require(wA >= 1, "wA must be >= 1")
  require(wB >= 1, "wB must be >= 1")
  require(wC >= 1, "wC must be >= 1")
  require(wZ >= 1, "wZ must be >= 1")
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wB.W))
    val C:  UInt = Input(UInt(wC.W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt(wZ.W))
  })
}
