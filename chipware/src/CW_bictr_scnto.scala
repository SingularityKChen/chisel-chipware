import chisel3._
import chisel3.experimental._

/**
  * == CW_bictr_scnto ==
  *
  * === Abstract ===
  *
  * CW_bictr_scnto is a variable width binary up/down counter, with static count-to flag.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | WIDTH | 1 to 30 | 3 | Width of the counter |
  * | COUNT_TO | 1 to 2<sup>WIDTH</sup> - 1 | 2 | Count to flag |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | data | WIDTH | Input | Data input |
  * | up_dn | 1 | Input | Up/Down control |
  * | cen | 1 | Input | Count enable |
  * | load | 1 | Input | Load enable |
  * | clk | 1 | Input | Clock |
  * | reset | 1 | Input | Reset |
  * | count | WIDTH | Output | Counter output |
  * | tercnt | 1 | Output | Terminal count flag |
  *
  * @param WIDTH Width of the counter
  * @param COUNT_TO Count to flag
  */
class CW_bictr_scnto(val WIDTH: Int = 3, val COUNT_TO: Int = 2)
    extends BlackBox(
      Map(
        "WIDTH" -> WIDTH,
        "COUNT_TO" -> COUNT_TO
      )
    ) {
  require(WIDTH >= 1 && WIDTH <= 30, s"WIDTH must be >= 1 and <= 30, but got $WIDTH")
  require(COUNT_TO >= 1 && COUNT_TO <= (1 << WIDTH) - 1, s"COUNT_TO must be >= 1 and <= 2^WIDTH - 1, but got $COUNT_TO")

  val io = IO(new Bundle {
    val data:   UInt  = Input(UInt(WIDTH.W))
    val up_dn:  Bool  = Input(Bool())
    val cen:    Bool  = Input(Bool())
    val load:   Bool  = Input(Bool())
    val clk:    Clock = Input(Clock())
    val reset:  Bool  = Input(Bool())
    val count:  UInt  = Output(UInt(WIDTH.W))
    val tercnt: Bool  = Output(Bool())
  })
}
