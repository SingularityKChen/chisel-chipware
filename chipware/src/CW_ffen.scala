import chisel3._
import chisel3.experimental._

/**
  * == CW_ffen ==
  *
  * === Abstract ===
  *
  * D Flip-Flop
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wD | >= 1 | 1 | D width and Q width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | CLK   | 1      | Input     | Clock input            |
  * | EN    | 1      | Input     | Load enable control           |
  * | D     | wD     | Input     | Register input             |
  * | Q     | wD     | Output    | Register output            |
  *
  * @param wD D width and Q width
  */
class CW_ffen(val wD: Int = 1)
    extends BlackBox(
      Map(
        "wD" -> wD
      )
    ) {
  require(wD >= 1, s"wD must be >= 1, but got $wD")

  val io = IO(new Bundle {
    val CLK: Clock = Input(Clock())
    val EN:  Bool  = Input(Bool())
    val D:   UInt  = Input(UInt(wD.W))
    val Q:   UInt  = Output(UInt(wD.W))
  })
}
