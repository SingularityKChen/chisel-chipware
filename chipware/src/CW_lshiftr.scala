import chisel3._
import chisel3.experimental._

/**
  * == CW_lshiftr ==
  *
  * === Abstract ===
  *
  * Logical Shift Right
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | wA >= 2      | 2            | A input width and Z output width |
  * | wSH           | wSH >= 1     | 1            | SH input width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A      | wA         | Input     | Input data   |
  * | SH     | wSH        | Input     | Number of positions to shift  |
  * | Z      | wA         | Output    | Shifted output data |
  *
  * @param wA  A input width and Z output width
  * @param wSH  SH input width
  */
class CW_lshiftr(val wA: Int = 2, val wSH: Int = 1)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wSH" -> wSH
      )
    ) {
  // Validation of all parameters
  require(wA >= 2, "wA must be >= 2")
  require(wSH >= 1, "wSH must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val SH: UInt = Input(UInt(wSH.W))
    val Z:  UInt = Output(UInt(wA.W))
  })
}
