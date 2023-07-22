import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_sum ==
  *
  * === Abstract ===
  *
  * Vector Adder ( Z = A0+A1+A2+... )
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | nI            | nI >= 2      | 2          | Number of input addends |
  * | wAi           | wAi >= 1     | 8          | Each input addend width |
  * | wZ            | wZ >= 1      | 8          | Z output width |
  *
  * === Ports ===
  *
  * | Name  | Width        | Direction | Description  |
  * |--------|--------------|-----------|------------------------|
  * | A      | wAi * nI     | Input     | Input addends |
  * | TC     | 1            | Input     | Control input. <br> If TC=0, unsigned addition is performed. <br> If TC=1, signed addition is performed. |
  * | Z      | wZ           | Output    | Sum output |
  *
  * @param nI  Number of input addends
  * @param wAi  Each input addend width
  * @param wZ  Z output width
  */
class CW_sum(val nI: Int = 2, val wAi: Int = 8, val wZ: Int = 8)
    extends BlackBox(
      Map(
        "nI" -> nI,
        "wAi" -> wAi,
        "wZ" -> wZ
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(nI >= 2, "nI must be >= 2")
  require(wAi >= 1, "wAi must be >= 1")
  require(wZ >= 1, "wZ must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt((wAi * nI).W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt(wZ.W))
  })
}
