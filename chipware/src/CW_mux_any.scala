import chisel3._
import chisel3.experimental._

/**
  * == CW_mux_any ==
  *
  * === Abstract ===
  *
  * Generalized Multibit Selection
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | >=1            | 8               | Width of A input |
  * | wS            | >=1            | 2               | Width of S input |
  * | wZ            | >=1            | 2               | Width of Z output |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A        | wA            | Input      | Data input |
  * | S        | wS            | Input      | Select input |
  * | Z        | wZ            | Output    | Data output |
  *
  * @param wA Width of A input
  * @param wS Width of S input
  * @param wZ Width of Z output
  */
class CW_mux_any(val wA: Int = 8, val wS: Int = 2, val wZ: Int = 2)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wS" -> wS,
        "wZ" -> wZ
      )
    ) {
  require(wA >= 1, s"wA must be >= 1, but got $wA")
  require(wS >= 1, s"wS must be >= 1, but got $wS")
  require(wZ >= 1, s"wZ must be >= 1, but got $wZ")
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val S: UInt = Input(UInt(wS.W))
    val Z: UInt = Output(UInt(wZ.W))
  })
}
