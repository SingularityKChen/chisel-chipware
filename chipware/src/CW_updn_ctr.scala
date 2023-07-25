import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_updn_ctr ==
  *
  * === Abstract ===
  *
  * CW_updn_ctr is a variable width binary up/down counter.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | WIDTH      | WIDTH >= 1   | 3          | Counter width |
  *
  * === Ports ===
  *
  * | Name    | Width      | Direction | Description          |
  * |---------|------------|-----------|----------------------|
  * | data    | (WIDTH-1):0 | Input     | Input data           |
  * | up_dn   | 1          | Input     | Up/Down control      |
  * | load    | 1          | Input     | Load control         |
  * | cen     | 1          | Input     | Clock enable         |
  * | clk     | 1          | Input     | Clock                |
  * | reset   | 1          | Input     | Reset                |
  * | count   | (WIDTH-1):0 | Output    | Counter output       |
  * | tercnt  | 1          | Output    | Terminal count output |
  *
  * @param WIDTH Counter width
  */
class CW_updn_ctr(val WIDTH: Int = 3) extends BlackBox(Map("WIDTH" -> WIDTH)) with HasBlackBoxPath {
  require(WIDTH >= 1, "WIDTH must be >= 1")

  val io = IO(new Bundle {
    val data:   UInt  = Input(UInt(WIDTH.W))
    val up_dn:  Bool  = Input(Bool())
    val load:   Bool  = Input(Bool())
    val cen:    Bool  = Input(Bool())
    val clk:    Clock = Input(Clock())
    val reset:  Bool  = Input(Bool())
    val count:  UInt  = Output(UInt(WIDTH.W))
    val tercnt: Bool  = Output(Bool())
  })
}
