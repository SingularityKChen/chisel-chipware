import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_exp2 ==
  *
  * === Abstract ===
  *
  * Combinatorial component for the fixed-point function
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | op_width      | 2 to 60      | 8            | Width of the ports a and z |
  * | arch          | 0 or 1       | 0            | 0 = Area optimized 1 = Speed optimized |
  * | err_range     | Unused       | 1            | Provided for compatibility. The component is always accurate to 1 ULP |
  *
  * === Ports ===
  *
  * | Name  | Width       | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | a      | op_width   | Input     | Input value in the range [0,1) |
  * | z      | op_width   | Output    | Output values in the range [1,2). MSB will always be 1 |
  * @param op_width  Width of the ports a and z
  * @param arch      0 = Area optimized 1 = Speed optimized
  * @param err_range Provided for compatibility. The component is always accurate to 1 ULP
  */
class CW_exp2(val op_width: Int = 8, val arch: Int = 0, val err_range: Int = 1)
    extends BlackBox(
      Map(
        "op_width" -> op_width,
        "arch" -> arch,
        "err_range" -> err_range
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(op_width >= 2 && op_width <= 60, "op_width must be in the range [2, 60]")
  require(arch == 0 || arch == 1, "arch must be 0 or 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a: UInt = Input(UInt(op_width.W))
    val z: UInt = Output(UInt(op_width.W))
  })
}
