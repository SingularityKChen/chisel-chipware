import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_fftapsen ==
  *
  * === Abstract ===
  *
  * Shift Register with Taps and Load Enable
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
  * | EN | 1 | Input | Loan enable control |
  * | D | wD | Input | Register input |
  * | Q | stages * wD | Output | Shift register output of each tap |
  *
  * @param wD D width
  * @param stages Number of pipeline stages or taps
  */
class CW_fftapsen(val wD: Int = 1, val stages: Int = 1)
    extends BlackBox(
      Map(
        "wD" -> wD,
        "stages" -> stages
      )
    )
    with HasBlackBoxPath {
  require(wD >= 1, s"wD must be >= 1, got $wD")
  require(stages >= 1, s"stages must be >= 1, got $stages")
  val io = IO(new Bundle {
    val CLK: Clock = Input(Clock())
    val EN:  Bool  = Input(Bool())
    val D:   UInt  = Input(UInt(wD.W))
    val Q:   UInt  = Output(UInt((stages * wD).W))
  })
}
