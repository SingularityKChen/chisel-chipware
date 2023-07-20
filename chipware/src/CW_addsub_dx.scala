import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath // To enable experimental features

/**
  *  == CW_addsub_dx ==
  *
  *  === Abstract ===
  *
  *  Duplex Adder/Subtractor with Saturation and Rounding
  *
  *  === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | >=4          | 4            | Word width of a, b, and sum |
  * | p1_width      | 2 to width-2 | 2            | Word width of part1 of duplex Add/Sub |
  * | p2_width      |              | width - p1_width | Word width of part2 of duplex Add/Sub |
  * | of_smplx_sat  |              | (1 << width) - 1 | Overflow value of simple mode |
  * | of_dplx_p1_sat|              | (1 << p1_width) - 1 | Overflow value of duplex mode part1 |
  * | of_dplx_p2_sat|              | (1 << p2_width) - 1 | Overflow value of duplex mode part2 |
  *
  *  === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | a      | width      | Input     | Input data |
  * | b      | width      | Input     | Input data |
  * | ci1    | 1          | Input     | Full or part1 carry input |
  * | ci2    | 1          | Input     | Part2 carry input |
  * | addsub | 1          | Input     | Add/subtract select input |
  * |        |            |           | 0 = performs add |
  * |        |            |           | 1 = performs subtract |
  * | tc     | 1          | Input     | Two's complement select (active high) |
  * | sat    | 1          | Input     | Saturation mode select (active high) |
  * | avg    | 1          | Input     | Average mode select (active high) |
  * | dplx   | 1          | Input     | Duplex mode select (active high) |
  * | sum    | width      | Output    | Output data |
  * | co1    | 1          | Output    | Part1 carry output |
  * | co2    | 1          | Output    | Full width or part2 carry output |
  *
  *  @param width Word width of a, b, and sum
  *  @param p1_width Word width of part1 of duplex Add/Sub
  */
class CW_addsub_dx(width: Int = 4, p1_width: Int = 2)
    extends BlackBox(
      Map(
        "width" -> width,
        "p1_width" -> p1_width
      )
    )
    with HasBlackBoxPath {
  require(width >= 4, s"width must be >= 4, got $width")
  require(p1_width >= 2 && p1_width <= width - 2, s"p1_width should in range [2, ${width - 2}], got $p1_width")
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt(width.W))
    val b:      UInt = Input(UInt(width.W))
    val ci1:    Bool = Input(Bool())
    val ci2:    Bool = Input(Bool())
    val addsub: Bool = Input(Bool())
    val tc:     Bool = Input(Bool())
    val sat:    Bool = Input(Bool())
    val avg:    Bool = Input(Bool())
    val dplx:   Bool = Input(Bool())
    val sum:    UInt = Output(UInt(width.W))
    val co1:    Bool = Output(Bool())
    val co2:    Bool = Output(Bool())
  })
}
