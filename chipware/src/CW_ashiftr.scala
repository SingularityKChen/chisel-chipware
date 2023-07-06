import chisel3._
import chisel3.util.log2Ceil
import chisel3.experimental._ // To enable experimental features

/**
  * == Arithmetic Shift Right ==
  *
  * Provides the arithmetic shift right function. Data that is shifted past the least significant bit (LSB) is discarded.
  * The sign bit, or most significant bit (MSB), is copied to all MSBs shifted in.
  * The width of the input data and the output are determined by the wA parameter.
  *
  * If wSH is less than expected, hardware is saved by omitting unnecessary stages from the shifter.
  * If wSH is larger than expected, the MSBs of SH represent extreme shifts.
  *
  * CW_ashiftr consists of wSH shift stages. Each stage is driven by the corresponding bit of SH.
  * The order of the staging is determined automatically, taking advantage of any timing skew on SH.
  *
  * == Features ==
  *   - Arithmetic right shifter.
  *   - The width of the shift control input signal is determined by the wSH parameter. The value of wSH is only meaningful up to bits.
  *   - Multiple extreme shift stages are redundant and can lead to hardware inefficiency.
  *   - The individual shift stages are built using multiplexor or and/or logic.
  *
  * == Port Description ==
  *
  * | Port | Type  | Size      | Description           |
  * |------|-------|-----------|-----------------------|
  * | A    | Input | wA bits   | Input data            |
  * | SH   | Input | wSH bits  | Number of positions to shift |
  * | Z    | Output| wA bits   | Shifted output data    |
  *
  * == Parameter Description ==
  *
  * | Parameter | Legal Range                | Default | Description                 |
  * |-----------|----------------------------|---------|-----------------------------|
  * | wA        | ≥ 2                        | 2       | A input width and Z output width |
  * | wSH       | 1 to ceil(log2(wA))        | 1       | SH input width              |
  *
  * == CW_ashiftr Behavior ==
  *
  * The following table illustrates the CW_ashiftr behavior for an input width of 6 bits (wA=6, wSH=3).
  */

class CW_ashiftr(val wA: Int = 2, val wSH: Int = 1) extends BlackBox(Map("wA" -> wA, "wSH" -> wSH)) {
  require(wA >= 2, "wA must be >= 2")
  require(Range(1, log2Ceil(wA) + 1).contains(wSH), f"wSH ${wSH} should in range [1, log2Ceil(${wA})]")
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val SH: UInt = Input(UInt(wSH.W))
    val Z:  UInt = Output(UInt(wA.W))
  })
}
