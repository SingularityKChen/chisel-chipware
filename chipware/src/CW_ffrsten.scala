import chisel3._
import chisel3.experimental._

/**
  * == CW_ffrsten ==
  *
  * === Abstract ===
  *
  * D Flip-Flop with Synchronous Reset and Load Enable
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|----------------|
  * | wD         | >= 1         | 1      | D, RSTD and Q width      |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |-------|--------|-----------|------------------------|
  * | CLK   | 1      | Input     | Clock input            |
  * | RST   | 1      | Input     | Synchronous reset control            |
  * | EN    | 1      | Input     | Load enable control           |
  * | D     | wD     | Input     | Register input             |
  * | RSTD  | wD     | Input     | Reset value       |
  * | Q     | wD     | Output    | Register output            |
  *
  * @param wD D, RSTD and Q width
  */
class CW_ffrsten(val wD: Int = 1)
    extends BlackBox(
      Map(
        "wD" -> wD
      )
    ) {
  require(wD >= 1, s"Data width must be >= 1, but got $wD")
  val io = IO(new Bundle {
    val CLK:  Clock = Input(Clock())
    val RST:  Bool  = Input(Bool())
    val EN:   Bool  = Input(Bool())
    val D:    UInt  = Input(UInt(wD.W))
    val RSTD: UInt  = Input(UInt(wD.W))
    val Q:    UInt  = Output(UInt(wD.W))
  })
}
