import chisel3._
import chisel3.experimental._

/**
  * == CW_gray2bin ==
  *
  * === Abstract ===
  *
  * Gray to Binary Converter
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | WIDTH         | WIDTH >= 1   | 3            | Input and output bit width |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | g      | WIDTH      | Input     | Gray coded input data |
  * | b      | WIDTH      | Output    | Binary coded output data |
  *
  * @param WIDTH  Input and output bit width
  */
class CW_gray2bin(val WIDTH: Int = 3)
    extends BlackBox(
      Map(
        "WIDTH" -> WIDTH
      )
    ) {
  // Validation of all parameters
  require(WIDTH >= 1, "WIDTH must be >= 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val g: UInt = Input(UInt(WIDTH.W))
    val b: UInt = Output(UInt(WIDTH.W))
  })
}
