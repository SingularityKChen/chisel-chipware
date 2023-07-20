import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath // To enable experimental features

/**
  * This component performs addition of integer inputs `A` and `B`, and a carry in `CI`, producing output `Z` and carry out `CO`.
  *
  * == Features ==
  *
  * - The bit width of `A` and `B` is parameterizable.
  *
  * == Port Description ==
  *
  * | Port Name | Type | Size (bits) | Description |
  * | :-------- | :---: | :--------- | :----------: |
  * | A | Input | wA | Integer input |
  * | B | Input | wA | Integer input |
  * | CI | Input | 1 | Carry inp |
  * | Z | Output | wA | Integer output |
  * | CO | Output | 1 | Carry out |
  *
  * == Parameter Description ==
  *
  * | Parameter Name | Legal Range | Default | Description |
  * | :------------- | :---------: | :-----: | :---------- |
  * | wA | >= 1 | 4 | Bit width of `A`, `B`, and `Z` |
  *
  * == Functional Description ==
  *
  * `{CO, Z} = A + B + CI`
  *
  * @param wA Bit width of `A`, `B`, and `Z`. Legal Range >= 1. Default: 4
  *
  * @example {{{
  * class add(val wA: Int = 4) extends Module {
  *   require(wA >= 1, "wA must be >= 1")
  *   val io = IO(new Bundle {
  *     val A: UInt = Input(UInt(wA.W))
  *     val B: UInt = Input(UInt(wA.W))
  *     val CI: UInt = Input(UInt(1.W))
  *     val Z: UInt = Output(UInt(wA.W))
  *     val CO: UInt = Output(UInt(1.W))
  *   })
  *   protected val U1: CW_add = Module(new CW_add(wA))
  *   U1.io.A := io.A
  *   U1.io.B := io.B
  *   U1.io.CI := io.CI
  *   io.Z := U1.io.Z
  *   io.CO := U1.io.CO
  * }
  * }}}
  */
class CW_add(val wA: Int = 4) extends BlackBox(Map("wA" -> wA)) with HasBlackBoxPath {
  require(wA >= 1, "wA must be >= 1")
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wA.W))
    val CI: UInt = Input(UInt(1.W))
    val Z:  UInt = Output(UInt(wA.W))
    val CO: UInt = Output(UInt(1.W))
  })
}
