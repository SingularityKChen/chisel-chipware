// filename: CW_8b10b_unbal.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_8b10b_unbal ==
  *
  * === Abstract ===
  *
  * CW_8b10b_unbal predicts (without fully encoding) whether a given character
  * will or will not flip the running disparity of the encoder when it is encoded.
  *
  * === Parameters ===
  *
  * | Parameter     | Legal Range  | Default      | Description            |
  * |---------------|--------------|--------------|------------------------|
  * | k28_5_only    | 0 to 1       | Default: 0   | Special character subset control parameter. 0 for all special characters available, 1 for only k28.5 available (when k_char = HIGH, regardless of the value on data_in). |
  *
  * === Ports ===
  *
  * | Name      | Width      | Direction | Description                |
  * |-----------|------------|-----------|----------------------------|
  * | k_char    | 1 bit      | Input     | Special character control input (LOW for data characters, HIGH for special characters) |
  * | data_in   | 8 bit      | Input     | Input for 8-bit data character to be encoded |
  * | unbal     | 1 bit      | Output    | Unbalanced code character indicator (LOW for balanced, HIGH for unbalanced) |
  *
  * @param k28_5_only Special character subset control parameter, 0 for all special characters available, 1 for only k28.5 available (when k_char = HIGH, regardless of the value on data_in)
  */
class CW_8b10b_unbal(val k28_5_only: Int = 0) extends BlackBox(Map("k28_5_only" -> k28_5_only)) with HasBlackBoxPath {
  require(
    k28_5_only >= 0 && k28_5_only <= 1,
    s"Incorrect Parameter value, k28_5_only = $k28_5_only (Valid range: 0 to 1)"
  )

  val io = IO(new Bundle {
    val k_char:  Bool = Input(Bool())
    val data_in: UInt = Input(UInt(8.W))
    val unbal:   Bool = Output(Bool())
  })
}
