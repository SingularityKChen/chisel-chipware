import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_ffrst ==
  *
  * === Abstract ===
  *
  * D Flip-Flop with Synchronous Reset
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wD | >= 1 | 1 | D, RSTD and Q width |
  *
  * === Ports ===
  * | Name  | Width  | Direction | Description  |
  * |-------|--------|-----------|------------------------|
  * | CLK   | 1      | Input     | Clock input            |
  * | RST   | 1      | Input     | Synchronous reset control            |
  * | D     | wD     | Input     | Register input             |
  * | RSTD  | wD     | Input     | Reset value       |
  * | Q     | wD     | Output    | Register output            |
  *
  * @param wD Data width
  */
class CW_ffrst(val wD: Int = 1)
    extends BlackBox(
      Map(
        "wD" -> wD
      )
    )
    with HasBlackBoxPath {
  require(wD >= 1, "wD must be >= 1")
  val io = IO(new Bundle {
    val CLK:  Clock = Input(Clock())
    val RST:  Bool  = Input(Bool())
    val D:    UInt  = Input(UInt(wD.W))
    val RSTD: UInt  = Input(UInt(wD.W))
    val Q:    UInt  = Output(UInt(wD.W))
  })
}
