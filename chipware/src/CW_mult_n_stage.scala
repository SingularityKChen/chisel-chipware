import chisel3._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_mult_n_stage ==
  *
  * === Abstract ===
  *
  * Simulation Architecture for CW_mult_n_stage
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            |              | 8            | Input A width |
  * | wB            |              | 8            | Input B width |
  * | stages        |              | 2            | Number of stages |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A     | wA         | Input     | Input A |
  * | B     | wB         | Input     | Input B |
  * | TC    | 1          | Input     | Test control |
  * | CLK   | 1          | Input     | Clock |
  * | Z     | wA + wB - 1 | Output    | Output Z |
  *
  * @param wA Input A width
  * @param wB Input B width
  * @param stages Number of stages
  */
class CW_mult_n_stage(val wA: Int = 8, val wB: Int = 8, val stages: Int = 2)
    extends BlackBox(
      Map(
        "wA" -> wA,
        "wB" -> wB,
        "stages" -> stages
      )
    )
    with HasBlackBoxPath {
  require(wA > 0, "wA must be greater than 0")
  require(wB > 0, "wB must be greater than 0")
  require(stages > 0, "stages must be greater than 0")

  val io = IO(new Bundle {
    val A:   UInt  = Input(UInt(wA.W))
    val B:   UInt  = Input(UInt(wB.W))
    val TC:  Bool  = Input(Bool())
    val CLK: Clock = Input(Clock())
    val Z:   UInt  = Output(UInt((wA + wB - 1).W))
  })
}
