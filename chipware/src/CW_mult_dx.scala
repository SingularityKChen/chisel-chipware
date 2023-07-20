// filename: CW_mult_dx.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_mult_dx ==
  *
  * === Abstract ===
  *
  * Duplex Multiplier
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range       | Default | Description                      |
  * |------------|-------------------|---------|----------------------------------|
  * | width      | >= 4              | 16      | Word width of a and b            |
  * | p1_width   | 2 to width - 2    | 8       | Word width of Part1 of duplex multiplier |
  *
  * === Ports ===
  *
  * | Name     | Width        | Direction | Description                       |
  * |----------|--------------|-----------|-----------------------------------|
  * | a        | width bit(s) | Input     | Input data                        |
  * | b        | width bit(s) | Input     | Input data                        |
  * | tc       | 1 bit        | Input     | Two's complement control          |
  * | dplx     | 1 bit        | Input     | Duplex mode select                |
  * | product  | width*2 bit(s) | Output  | Product                           |
  *
  * @param width    Word width of a and b
  * @param p1_width Word width of Part1 of duplex multiplier
  */
class CW_mult_dx(val width: Int = 16, val p1_width: Int = 8)
    extends BlackBox(
      Map(
        "width" -> width,
        "p1_width" -> p1_width
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 4, "width must be >= 4")
  require(p1_width >= 2 && p1_width <= width - 2, "p1_width should be in range [2, width - 2]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val a:       UInt = Input(UInt(width.W))
    val b:       UInt = Input(UInt(width.W))
    val tc:      Bool = Input(Bool())
    val dplx:    Bool = Input(Bool())
    val product: UInt = Output(UInt((width * 2).W))
  })
}
