import chisel3._
import chisel3.experimental._ // To enable experimental features

/**
  * == CW_addsub ==
  *
  * === Abstract ===
  *
  * Adder-Subtracter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA | >= 1 | 4 | Bit width of A, B, and Z |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A | wA | Input | Integer input |
  * | B | wA | Input | Integer input |
  * | CI | 1 | Input | Carry in |
  * | SUB | 1 | Input | Determines operation: 0: Addition 1: Subtraction |
  * | Z | wA | Output | Integer output |
  * | CO | 1 | Output | Carry out |
  *
  * @param wA  A and B input width and Z output width
  */
class CW_addsub(val wA: Int = 8)
    extends BlackBox(
      Map(
        "wA" -> wA
      )
    ) {
  require(wA >= 1, "wA must be >= 1")
  val io = IO(new Bundle {
    val A:   UInt = Input(UInt(wA.W))
    val B:   UInt = Input(UInt(wA.W))
    val CI:  Bool = Input(Bool())
    val SUB: Bool = Input(Bool())
    val Z:   UInt = Output(UInt(wA.W))
    val CO:  Bool = Output(Bool())
  })
}
