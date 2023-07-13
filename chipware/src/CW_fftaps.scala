import chisel3._
import chisel3.experimental._

/**
  * == CW_fftaps ==
  *
  * === Abstract ===
  *
  * Shift Register with Taps.
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wD | >= 1 | 1 | D width |
  * | stages | >= 1 | 1 | Number of pipeline stages or taps |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | CLK | 1 | Input | Clock input |
  * | D | wD | Input | Register input |
  * | Q | stages*wD | Output | Shift register output of each tap |
  *
  * @param wD D width
  * @param stages Number of pipeline stages or taps
  */
class CW_fftaps(val wD: Int = 1, val stages: Int = 1)
    extends BlackBox(
      Map(
        "wD" -> wD,
        "stages" -> stages
      )
    ) {
  require(wD >= 1, "wD must be >= 1")
  require(stages >= 1, "stages must be >= 1")
  protected val bit_width_Q: Int = stages * wD
  val io = IO(new Bundle {
    val CLK: Clock = Input(Clock())
    val D:   UInt  = Input(UInt(wD.W))
    val Q:   UInt  = Output(UInt(bit_width_Q.W))
  })
}
