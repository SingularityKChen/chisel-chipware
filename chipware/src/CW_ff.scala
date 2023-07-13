// filename: CW_ff.scala
import chisel3._
import chisel3.experimental._

/**
  * == CW_ff ==
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
  * | D     | wD     | Input     | Register input             |
  * | Q     | wD     | Output    | Register output            |
  *
  * @param wD  D input width and Q output width
  */
class CW_ff(val wD: Int = 1)
    extends BlackBox(
      Map(
        "wD" -> wD
      )
    ) {
  // Validation of all parameters
  require(wD >= 1, "wD must be >= 1")
  val io = IO(new Bundle {
    val CLK: Clock = Input(Clock())
    val D:   UInt  = Input(UInt(wD.W))
    val Q:   UInt  = Output(UInt(wD.W))
  })
}
