import chisel3._
import chisel3.experimental._

/**
  * == CW_bin2gray ==
  *
  * === Abstract ===
  *
  * Binary to Gray Converter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | WIDTH |  | 4 |  |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | b | WIDTH | Input |  |
  * | g | WIDTH | Output |  |
  * @param WIDTH  Width of the input and output
  */
class CW_bin2gray(val WIDTH: Int = 4)
    extends BlackBox(
      Map(
        "WIDTH" -> WIDTH
      )
    ) {
  require(WIDTH > 0, "WIDTH must be greater than 0")
  val io = IO(new Bundle {
    val b: UInt = Input(UInt(WIDTH.W))
    val g: UInt = Output(UInt(WIDTH.W))
  })
}
