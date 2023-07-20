// filename: CW_blend.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_blend ==
  *
  * === Abstract ===
  *
  * Performs linear interpolation between two points, X0 and X1.
  * The TC input controls the arithmetic format.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wX  | >= 1  | 8  | X data width |
  * | wA  | >= 1  | 8  | Alpha fraction width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | X0  | wX  | Input | Input data |
  * | X1  | wX  | Input | Input data |
  * | TC  | 1  | Input | Signed/Unsigned control input |
  * | Alpha  | wA  | Input | Alpha fraction |
  * | Alpha1  | 1  | Input | Used for alpha of 1.0 |
  * | Z  | wX+wA  | Output | Blended result |
  *
  * @param wX  >= 1, input width
  * @param wA  >= 1, input width
  */
class CW_blend(val wX: Int = 8, val wA: Int = 8)
    extends BlackBox(
      Map(
        "wX" -> wX,
        "wA" -> wA
      )
    )
    with HasBlackBoxPath {
  require(wX >= 1, s"wX must be >= 1, but got $wX")
  require(wA >= 1, s"wA must be >= 1, but got $wA")

  val io = IO(new Bundle {
    val X0:     UInt = Input(UInt(wX.W))
    val X1:     UInt = Input(UInt(wX.W))
    val TC:     Bool = Input(Bool())
    val Alpha:  UInt = Input(UInt(wA.W))
    val Alpha1: Bool = Input(Bool())
    val Z:      UInt = Output(UInt((wX + wA).W))
  })
}
