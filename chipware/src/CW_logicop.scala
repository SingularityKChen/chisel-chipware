import chisel3._
import chisel3.util._
import chisel3.experimental._

/** == CW_logicop ==
  *
  * === Abstract ===
  *
  * Logical Operation
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | wS         | >= 1         | 8        | S, D, and Z widths |
  *
  * === Ports ===
  *
  * | Name | Width   | Direction | Description |
  * |------|---------|-----------|-------------|
  * | S    | wS      | Input     | S input data |
  * | D    | wS      | Input     | D input data  |
  * | OP   | 4       | Input     | Operation control input  |
  * | Z    | wS      | Output    | Output result  |
  *
  * @param wS S, D, and Z widths
  */
class CW_logicop(val wS: Int = 8) extends BlackBox(Map("wS" -> wS)) with HasBlackBoxPath {
  require(wS >= 1, "wS must be >= 1")

  val io = IO(new Bundle {
    val S:  UInt = Input(UInt(wS.W))
    val D:  UInt = Input(UInt(wS.W))
    val OP: UInt = Input(UInt(4.W))
    val Z:  UInt = Output(UInt(wS.W))
  })
}
