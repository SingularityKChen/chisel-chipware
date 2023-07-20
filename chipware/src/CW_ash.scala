import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_ash ==
  *
  * === Abstract ===
  *
  * General Purpose Arithmetic Shifter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA  | >= 2  | 4 | A input width and Z output width |
  * | wSH  | >= 1  | 2 | SH input width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A  | wA  | Input | Input data |
  * | DATA_TC  | 1  | Input | 0 if A is unsigned or 1 if A is signed |
  * | SH  | wSH  | Input | Shift amount |
  * | SH_TC  | 1  | Input | 0 if SH is unsigned or 1 if SH is signed |
  * | Z  | wA  | Output | Output result |
  *
  * @param wA  A input width and Z output width
  * @param wSH  SH input width
  */
class CW_ash(val wA: Int = 4, val wSH: Int = 2)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wSH" -> wSH
      )
    )
    with HasBlackBoxPath {
  require(wA >= 2, s"wA must be >= 2, but got $wA")
  require(wSH >= 1, s"wSH must be >= 1, but got $wSH")

  val io = IO(new Bundle {
    val A:       UInt = Input(UInt(wA.W))
    val DATA_TC: Bool = Input(Bool())
    val SH:      UInt = Input(UInt(wSH.W))
    val SH_TC:   Bool = Input(Bool())
    val Z:       UInt = Output(UInt(wA.W))
  })
}
