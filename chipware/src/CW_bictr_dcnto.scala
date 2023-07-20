import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_bictr_dcnto ==
  *
  * === Abstract ===
  *
  * CW_bictr_dcnto is a variable width binary up/down counter, with
  * dynamic count-to flag.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | WIDTH      | >= 1          | 3          | Counter width  |
  *
  * === Ports ===
  *
  * | Name      | Width         | Direction | Description          |
  * |-----------|---------------|-----------|----------------------|
  * | data      | WIDTH         | Input     | Data input           |
  * | count_to  | WIDTH         | Input     | Count-to input       |
  * | up_dn     | 1             | Input     | Up/Down control      |
  * | load      | 1             | Input     | Load control         |
  * | cen       | 1             | Input     | Clock enable         |
  * | clk       | 1             | Input     | Clock input          |
  * | reset     | 1             | Input     | Reset input          |
  * | count     | WIDTH         | Output    | Count output         |
  * | tercnt    | 1             | Output    | Terminal count output |
  *
  * @param WIDTH  Counter width
  */
class CW_bictr_dcnto(val WIDTH: Int = 3) extends BlackBox(Map("WIDTH" -> WIDTH)) with HasBlackBoxPath {
  // Validation of the parameter
  require(WIDTH >= 1, "WIDTH must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val data:     UInt  = Input(UInt(WIDTH.W))
    val count_to: UInt  = Input(UInt(WIDTH.W))
    val up_dn:    Bool  = Input(Bool())
    val load:     Bool  = Input(Bool())
    val cen:      Bool  = Input(Bool())
    val clk:      Clock = Input(Clock())
    val reset:    Bool  = Input(Bool())
    val count:    UInt  = Output(UInt(WIDTH.W))
    val tercnt:   Bool  = Output(Bool())
  })
}
