import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_pipe_reg ==
  *
  * === Abstract ===
  *
  * Pipeline Register/Delay Line
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | stages     | stages >= 0  | 1        | Number of pipeline stages |
  * | wD         | wD >= 1      | 1        | D input width and Q output width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |-------|--------|-----------|--------------|
  * | D     | wD     | Input     | Input to pipeline register |
  * | CLK   | 1      | Input     | Clock input  |
  * | Q     | wD     | Output    | Output from pipeline register  |
  *
  * @param stages Number of pipeline stages
  * @param wD     D input width and Q output width
  */
class CW_pipe_reg(val stages: Int = 1, val wD: Int = 1)
    extends BlackBox(
      Map(
        "stages" -> stages,
        "wD" -> wD
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(stages >= 0, "stages must be >= 0")
  require(wD >= 1, "wD must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val D:   UInt  = Input(UInt(wD.W))
    val CLK: Clock = Input(Clock())
    val Q:   UInt  = Output(UInt(wD.W))
  })
}
