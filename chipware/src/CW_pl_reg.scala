import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_pl_reg ==
  *
  * === Abstract ===
  *
  * Pipeline Register
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | width         | 1 <= width   | 1            | Width of the data_in and data_out ports |
  * | in_reg        | 0 or 1       | 0            | Flag to indicate presence of input register: <br> 0 = no input register <br> 1 = input register used |
  * | stages        | 1 <= stages  | 1            | Determines number of pipeline stages (number of pipeline stages = stages-1) |
  * | out_reg       | 0 or 1       | 0            | Flag to indicate presence of output register: <br> 0 = no output register <br> 1 = output register used |
  * | rst_mode      | 0 or 1       | 0            | Reset mode     |
  *
  * === Ports ===
  *
  * | Name     | Width                   | Direction | Description  |
  * |----------|-------------------------|-----------|--------------|
  * | clk      | 1                       | Input     | Clock        |
  * | rst_n    | 1                       | Input     | Reset, active-low. Flush all data to zero when low. This is independent of whether the enable bits are set. |
  * | enable   | stages-2+in_reg+out_reg | Input     | Individual enable bits for each register. enable[i] enables data prorogation from register[i] to register[i+1]. When enable[i] is low, then data between these two resisters is stalled. |
  * | data_in  | width                   | Input     | Input data stream |
  * | data_out | width                   | Output    | Output data stream. This is equal to the input data delayed by stages-1+in_reg+out_reg cycles. |
  *
  * @param width    Width of the data_in and data_out ports
  * @param in_reg   Flag to indicate presence of input register: 0 = no input register; 1 = input register used
  * @param stages   Determines number of pipeline stages (number of pipeline stages = stages-1)
  * @param out_reg  Flag to indicate presence of output register: 0 = no output register; 1 = output register used
  * @param rst_mode Reset mode
  */
class CW_pl_reg(
  val width:    Int = 1,
  val in_reg:   Int = 0,
  val stages:   Int = 1,
  val out_reg:  Int = 0,
  val rst_mode: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "in_reg" -> in_reg,
        "stages" -> stages,
        "out_reg" -> out_reg,
        "rst_mode" -> rst_mode
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 1, "width must be >= 1")
  require(in_reg == 0 || in_reg == 1, "in_reg must be 0 or 1")
  require(stages >= 1, "stages must be >= 1")
  require(out_reg == 0 || out_reg == 1, "out_reg must be 0 or 1")
  require(in_reg != 1 || out_reg != 1, "Cannot have both in_reg and out_reg set")
  require(rst_mode == 0 || rst_mode == 1, "rst_mode must be 0 or 1")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val rst_n:    Bool  = Input(Bool())
    val enable:   UInt  = Input(UInt((stages - 1 + in_reg + out_reg).W))
    val data_in:  UInt  = Input(UInt(width.W))
    val data_out: UInt  = Output(UInt(width.W))
  })
}
