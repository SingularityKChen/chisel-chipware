import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_debugger ==
  *
  * === Abstract ===
  *
  * On-Chip ASCII Debugger
  *
  * === Parameters ===
  *
  * | Parameter     | Legal Range         | Default  | Description                      |
  * |---------------|---------------------|----------|----------------------------------|
  * | rd_bits_width | 8 to 2<sup>11</sup> | 8        | width of rd_bits                 |
  * | wr_bits_width | 8 to 2<sup>11</sup> | 8        | width of wr_bits                 |
  * | clk_freq      | >= 1                | 1        | clock frequency in MHz           |
  * | baud_rate     | 300, 600, 1200, ... | 19200    | Baud rate                        |
  * | mark_parity   | 0 to 1              | 1        | Sets the value of parity bit     |
  *
  * === Ports ===
  *
  * | Name            | Width         | Direction | Description                       |
  * |-----------------|---------------|-----------|-----------------------------------|
  * | clk             | 1 bit         | Input     | clock                             |
  * | reset_n         | 1 bit         | Input     | synchronous reset, active low     |
  * | div_bypass_mode | 1 bit         | Input     | clock divider bypass, active high |
  * | rd_bits         | rd_bits_width | Input     | input data bus                    |
  * | rxd             | 1 bit         | Input     | receive data                      |
  * | wr_bits         | wr_bits_width | Output    | output data bus                   |
  * | txd             | 1 bit         | Output    | transmit data                     |
  *
  * @param rd_bits_width Width of rd_bits
  * @param wr_bits_width Width of wr_bits
  * @param clk_freq Clock frequency in MHz
  * @param baud_rate Baud rate
  * @param mark_parity Sets the value of parity bit
  */
class CW_debugger(
  val rd_bits_width: Int = 8,
  val wr_bits_width: Int = 8,
  val clk_freq:      Int = 1,
  val baud_rate:     Int = 19200,
  val mark_parity:   Int = 1)
    extends BlackBox(
      Map(
        "rd_bits_width" -> rd_bits_width,
        "wr_bits_width" -> wr_bits_width,
        "clk_freq" -> clk_freq,
        "baud_rate" -> baud_rate,
        "mark_parity" -> mark_parity
      )
    )
    with HasBlackBoxPath {
  require(rd_bits_width >= 8 && rd_bits_width <= 2048, "rd_bits_width must be in range [8, 2048]")
  require(wr_bits_width >= 8 && wr_bits_width <= 2048, "wr_bits_width must be in range [8, 2048]")
  require(clk_freq >= 1, "clk_freq must be >= 1")
  require(
    Seq(300, 600, 1200, 2400, 4800, 9600, 19200).contains(baud_rate),
    "baud_rate must be one of [300, 600, 1200, 2400, 4800, 9600, 19200]"
  )
  require(mark_parity >= 0 && mark_parity <= 1, "mark_parity must be in range [0, 1]")

  val io = IO(new Bundle {
    val clk:             Clock = Input(Clock())
    val reset_n:         Bool  = Input(Bool())
    val div_bypass_mode: Bool  = Input(Bool())
    val rd_bits:         UInt  = Input(UInt(rd_bits_width.W))
    val rxd:             Bool  = Input(Bool())
    val wr_bits:         UInt  = Output(UInt(wr_bits_width.W))
    val txd:             Bool  = Output(Bool())
  })
}
