// filename: CW_cmp_dx.scala
import chisel3._
import chisel3.experimental._

/**
  * == CW_cmp_dx ==
  *
  * === Abstract ===
  *
  * Duplex Comparator
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | width      | >= 4         | 8        | Word width of a and b |
  * | p1_width   | 2 to width-2 | 4        | Word width of part1 of duplex compare |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |-------|--------|-----------|--------------|
  * | a     | width  | Input     | Input data   |
  * | b     | width  | Input     | Input data   |
  * | tc    | 1      | Input     | Two's complement control |
  * | dplx  | 1      | Input     | Duplex mode select (active high) |
  * | lt1   | 1      | Output    | Part1 : less-than output condition |
  * | eq1   | 1      | Output    | Part1 : equal output condition |
  * | gt1   | 1      | Output    | Part1 : greater-than output condition |
  * | lt2   | 1      | Output    | Full width or part2 : less-than output condition |
  * | eq2   | 1      | Output    | Full width or part2 : equal output condition |
  * | gt2   | 1      | Output    | Full width or part2 : greater-than output condition |
  *
  * @param width  Word width of a and b
  * @param p1_width  Word width of part1 of duplex compare
  */
class CW_cmp_dx(val width: Int = 8, val p1_width: Int = 4)
    extends BlackBox(
      Map(
        "width" -> width,
        "p1_width" -> p1_width
      )
    ) {
  // Validation of all parameters
  require(width >= 4, "width must be >= 4")
  require(p1_width >= 2 && p1_width <= width - 2, "p1_width should be in range [2, width-2]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:    UInt = Input(UInt(width.W))
    val b:    UInt = Input(UInt(width.W))
    val tc:   Bool = Input(Bool())
    val dplx: Bool = Input(Bool())
    val lt1:  Bool = Output(Bool())
    val eq1:  Bool = Output(Bool())
    val gt1:  Bool = Output(Bool())
    val lt2:  Bool = Output(Bool())
    val eq2:  Bool = Output(Bool())
    val gt2:  Bool = Output(Bool())
  })
}
