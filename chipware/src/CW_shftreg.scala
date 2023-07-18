import chisel3._
import chisel3.experimental._

/**
  * == CW_shftreg ==
  *
  * === Abstract ===
  *
  * Shift Register with Serial Input, Parallel Load, Serial Output, and Parallel Output
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | length | >= 1 | 4 | Length of shifter |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | clk | 1 | Input | Clock input |
  * | s_in | 1 | Input | Serial shift input |
  * | p_in | length | Input | Parallel input |
  * | shift_n | 1 | Input | Shift enable, active low |
  * | load_n | 1 | Input | Parallel load enable, active low |
  * | p_out | length | Output | Parallel output |
  *
  * @param length Length of shifter
  */
class CW_shftreg(val length: Int = 4)
    extends BlackBox(
      Map(
        "length" -> length
      )
    ) {
  // Validation of all parameters
  require(length >= 1, "length must be >= 1")
  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:     Clock = Input(Clock())
    val s_in:    Bool  = Input(Bool())
    val p_in:    UInt  = Input(UInt(length.W))
    val shift_n: Bool  = Input(Bool())
    val load_n:  Bool  = Input(Bool())
    val p_out:   UInt  = Output(UInt(length.W))
  })
}
