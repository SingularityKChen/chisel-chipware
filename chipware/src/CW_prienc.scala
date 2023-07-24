import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_prienc ==
  *
  * === Abstract ===
  *
  * Priority Encoder
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA  | wA >= 1  | 2  | Width of A  |
  * | wZ  | wZ >= 1  | 2  | Width of Z  |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A  | wA  | Input  | Input data  |
  * | Z  | wZ  | Output  | Output result  |
  *
  * @param wA  Width of A
  * @param wZ  Width of Z
  */
class CW_prienc(val wA: Int = 2, val wZ: Int = 2)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wZ" -> wZ
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(wA >= 1, "wA must be >= 1")
  require(wZ >= 1, "wZ must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val Z: UInt = Output(UInt(wZ.W))
  })
}
