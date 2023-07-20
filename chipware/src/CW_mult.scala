// filename: CW_mult.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_mult ==
  *
  * === Abstract ===
  *
  * Simulation Architecture for CW_mult
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | wA         | wA >= 1      | 8        | A input width|
  * | wB         | wB >= 1      | 8        | B input width|
  *
  * === Ports ===
  *
  * | Name  | Width        | Direction | Description  |
  * |-------|--------------|-----------|--------------|
  * | A     | wA           | Input     | A input      |
  * | B     | wB           | Input     | B input      |
  * | TC    | 1            | Input     | TC input     |
  * | Z     | wA + wB - 1  | Output    | Z output     |
  *
  * @param wA  A input width
  * @param wB  B input width
  */
class CW_mult(val wA: Int = 8, val wB: Int = 8)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wB" -> wB
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 1, "wA must be >= 1")
  require(wB >= 1, "wB must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wB.W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt((wA + wB - 1).W))
  })
}
