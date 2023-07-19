import chisel3._
import chisel3.experimental._

/**
  * == CW_prod_sum ==
  *
  * === Abstract ===
  *
  * Sum of Products
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wAi          | wAi >= 1     | 4            | Width of words in A |
  * | wBi          | wBi >= 1     | 5            | Width of words in B |
  * | numinputs    | numinputs >= 1 | 4          | Number of inputs in A and B |
  * | wZ           | wZ >= 1      | 12           | Z output width |
  *
  * === Ports ===
  *
  * | Name  | Width            | Direction | Description  |
  * |--------|------------------|-----------|------------------------|
  * | A      | wAi * numinputs  | Input     | Input data |
  * | B      | wBi * numinputs  | Input     | Input data |
  * | TC     | 1                | Input     | Control input. With TC=0, the operation is unsigned. With TC=1, the operation is signed. |
  * | Z      | wZ               | Output    | Output result |
  * @param wAi  Width of words in A
  * @param wBi  Width of words in B
  * @param numinputs  Number of inputs in A and B
  * @param wZ  Width of Z
  */
class CW_prod_sum(val wAi: Int = 4, val wBi: Int = 5, val numinputs: Int = 4, val wZ: Int = 12)
    extends BlackBox(
      Map(
        "wAi" -> wAi,
        "wBi" -> wBi,
        "numinputs" -> numinputs,
        "wZ" -> wZ
      )
    ) {
  // Validation of all parameters
  require(wAi >= 1, "wAi must be >= 1")
  require(wBi >= 1, "wBi must be >= 1")
  require(numinputs >= 1, "numinputs must be >= 1")
  require(wZ >= 1, "wZ must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt((wAi * numinputs).W))
    val B:  UInt = Input(UInt((wBi * numinputs).W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt(wZ.W))
  })
}
