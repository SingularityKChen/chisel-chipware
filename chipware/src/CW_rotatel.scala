import chisel3._
import chisel3.util._
import chisel3.experimental._

/**
  * == CW_rotatel ==
  *
  * === Abstract ===
  *
  * Rotate Left
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Range  | Default  | Description                           |
  * |--------------|--------------|----------|---------------------------------------|
  * | wA           | >= 2         | 2        | A input width and Z output width |
  * | wSH          | >= 1         | 1        | SH input width                   |
  *
  * === Ports ===
  *
  * | Name         | Width        | Direction| Description                           |
  * |--------------|--------------|----------|---------------------------------------|
  * | A            | wA           | Input    | Input data                           |
  * | SH           | wSH          | Input    | Number of positions to rotate        |
  * | Z            | wA           | Output   | Rotated output data                  |
  *
  * @param wA          A input width and Z output width
  * @param wSH         SH input width
  */
class CW_rotatel(val wA: Int, val wSH: Int)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wSH" -> wSH
      )
    )
    with HasBlackBoxPath {

  // Validate parameters
  require(wA >= 2, s"wA must be >= 2")
  require(wSH >= 1, s"wSH must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val SH: UInt = Input(UInt(wSH.W))
    val Z:  UInt = Output(UInt(wA.W))
  })
}
