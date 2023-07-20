import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_ecc ==
  *
  * === Abstract ===
  *
  * Error Checking and Correction
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |------------|--------------|----------|--------------|
  * | width      | 8 to 8178    | 32       | Width of input and output data buses |
  * | chkbits    | 5 to 14      | 7        | Width of check bits input and output buses |
  * | synd_sel   | 0 or 1       | 0        | Selects function of chkout |
  *
  * === Ports ===
  *
  * | Name       | Width        | Direction | Description  |
  * |------------|--------------|-----------|--------------|
  * | gen        | 1 bit        | Input     | Suppresses correction in write mode (gen = 1) and generates check bits. Enables correction when in read mode (gen = 0) and correct_n is asserted |
  * | correct_n  | 1 bit        | Input     | Enables correction of correctable words |
  * | datain     | width bits   | Input     | Input data word to check, or data from which check bits are generated |
  * | chkin      | chkbits bits | Input     | Check bits input for error analysis on read |
  * | err_detect | 1 bit        | Output    | Indicates that an error has been detected |
  * | err_multpl | 1 bit        | Output    | Indicates that the error detected is a multiple-bit error and, therefore, uncorrectable |
  * | dataout    | width bits   | Output    | Output data |
  * | chkout     | chkbits bits | Output    | When gen = 1, chkout contains the check bits. When gen = 0 and synd_sel = 0, chkout is the corrected or uncorrected data. When gen = 0 and synd_sel= 1, chkout is the error syndrome value |
  *
  * @param width    Width of input and output data buses
  * @param chkbits  Width of check bits input and output buses
  * @param synd_sel Selects function of chkout
  */
class CW_ecc(val width: Int = 32, val chkbits: Int = 7, val synd_sel: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "chkbits" -> chkbits,
        "synd_sel" -> synd_sel
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 8 && width <= 8178, "width must be in the range [8, 8178]")
  require(chkbits >= 5 && chkbits <= 14, "chkbits must be in the range [5, 14]")
  require(synd_sel == 0 || synd_sel == 1, "synd_sel must be 0 or 1")
  require(width <= ((1 << (chkbits - 1)) - chkbits), s"chkbits = $chkbits is low for the specified width = $width")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val gen:        Bool = Input(Bool())
    val correct_n:  Bool = Input(Bool())
    val datain:     UInt = Input(UInt(width.W))
    val chkin:      UInt = Input(UInt(chkbits.W))
    val err_detect: Bool = Output(Bool())
    val err_multpl: Bool = Output(Bool())
    val dataout:    UInt = Output(UInt(width.W))
    val chkout:     UInt = Output(UInt(chkbits.W))
  })
}
