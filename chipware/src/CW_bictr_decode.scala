import chisel3._
import chisel3.experimental._

/**
  * == CW_bictr_decode ==
  *
  * === Abstract ===
  *
  * CW_bictr_decode is a variable width binary up/down counter, whose output count is decoded.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | WIDTH      | 1 to 32      | 3        | Width of the counter |
  *
  * === Ports ===
  *
  * | Name      | Width | Direction | Description |
  * |-----------|-------|-----------|-------------|
  * | data      | WIDTH | Input     | Data input  |
  * | up_dn     | 1     | Input     | Up/Down control |
  * | cen       | 1     | Input     | Enable control |
  * | load      | 1     | Input     | Load control |
  * | clk       | 1     | Input     | Clock input |
  * | reset     | 1     | Input     | Reset input |
  * | count_dec | 2<sup>WIDTH</sup> | Output | Decoded counter output |
  * | tercnt    | 1     | Output    | Terminal count output |
  *
  * @param WIDTH Width of the counter
  */
class CW_bictr_decode(val WIDTH: Int = 3)
    extends BlackBox(
      Map(
        "WIDTH" -> WIDTH
      )
    ) {
  require(WIDTH > 0 && WIDTH <= 32, "WIDTH must be between 1 and 32")
  val io = IO(new Bundle {
    val data:      UInt  = Input(UInt(WIDTH.W))
    val up_dn:     Bool  = Input(Bool())
    val cen:       Bool  = Input(Bool())
    val load:      Bool  = Input(Bool())
    val clk:       Clock = Input(Clock())
    val reset:     Bool  = Input(Bool())
    val count_dec: UInt  = Output(UInt((1 << WIDTH).W))
    val tercnt:    Bool  = Output(Bool())
  })
}
