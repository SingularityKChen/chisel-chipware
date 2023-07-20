import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_shifter ==
  *
  * === Abstract ===
  *
  * Combined Arithmetic and Barrel Shifter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | data_width | >= 2 | 8 | Word length of data_in and data_out |
  * | sh_width | 1 to (ceil(log2[data_width]) + 1) | 3 | Word length of sh |
  * | inv_mode | 0 to 3 | 0 | logic mode |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | data_in | data_width bit(s) | Input | Input data |
  * | data_tc | 1 bit | Input | Two's complement control on data_in |
  * | sh | sh_width bit(s) | Input | Shift control |
  * | sh_tc | 1 bit | Input | Two's complement control on sh |
  * | sh_mode | 1 bit | Input | Arithmetic or barrel shift mode |
  * | data_out | data_width bit(s) | Output | Output data |
  *
  * @param data_width Word length of data_in and data_out
  * @param sh_width Word length of sh
  * @param inv_mode logic mode
  */
class CW_shifter(val data_width: Int = 8, val sh_width: Int = 3, val inv_mode: Int = 0)
    extends BlackBox(
      Map(
        "data_width" -> data_width,
        "sh_width" -> sh_width,
        "inv_mode" -> inv_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(data_width >= 2, "data_width must be >= 2")
  require(
    sh_width >= 1 && sh_width <= log2Ceil(data_width) + 1,
    "sh_width should be in range [1, log2Ceil(data_width) + 1]"
  )
  require(inv_mode >= 0 && inv_mode <= 3, "inv_mode should be in range [0, 3]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val data_in:  UInt = Input(UInt(data_width.W))
    val data_tc:  Bool = Input(Bool())
    val sh:       UInt = Input(UInt(sh_width.W))
    val sh_tc:    Bool = Input(Bool())
    val sh_mode:  Bool = Input(Bool())
    val data_out: UInt = Output(UInt(data_width.W))
  })
}
