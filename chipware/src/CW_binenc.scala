// filename: CW_binenc.scala
import chisel3._
import chisel3.util._
import chisel3.experimental._

/**
  * == CW_binenc ==
  *
  * === Abstract ===
  *
  * Determines the bit position of the least significant 1 in the A input signal.
  * The result is provided as a binary number on the Z output.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA | >=1 | 2 | A input width |
  * | wZ | >=1 | 1 | Z output width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A | wA | Input | Input data |
  * | Z | wZ | Output | Binary encoded output data |
  * | ERR | 1 | Output | Error indication that A=0 |
  *
  * @param wA  A input width
  * @param wZ  Z output width
  */
class CW_binenc(val wA: Int = 2, val wZ: Int = 1)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wZ" -> wZ
      )
    ) {
  require(wA >= 1, s"wA must be >= 1, but got $wA")
  require(wZ >= 1, s"wZ must be >= 1, but got $wZ")
  protected val bit_width_wA: Int = log2Ceil(wA)
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(wA.W))
    val Z:   UInt = Output(UInt(wZ.W))
    val ERR: Bool = Output(Bool())
  })
}
