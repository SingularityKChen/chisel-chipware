import chisel3._
import chisel3.experimental._

/**
  * == CW_shad_reg ==
  *
  * === Abstract ===
  *
  * Shadow and Multibit Register
  *
  * === Parameters ===
  *
  * | Parameter     | Legal Range | Default | Description |
  * |---------------|-------------|---------|-------------|
  * | width         | 1 to 512    | 8       | Word width input,output and registers |
  * | bld_shad_reg  | 1 or 0      | 0       | If one build both system register and shadow register |
  *
  * === Ports ===
  *
  * | Name      | Width | Direction | Description |
  * |-----------|-------|-----------|-------------|
  * | datain    | width | Input     | Input data |
  * | sys_clk   | 1     | Input     | Clock that samples the system registers |
  * | shad_clk  | 1     | Input     | Clock that samples the output of system to shadow register |
  * | reset     | 1     | Input     | Asynchronous active low reset |
  * | SI        | 1     | Input     | Serial scan input, clocked by shad_clk when SE is high |
  * | SE        | 1     | Input     | Serial scan enable signal, active high. Enables scan only on shadow registers |
  * | sys_out   | width | Output    | Output of system registers |
  * | shad_out  | width | Output    | Parallel output of shadow registers |
  * | SO        | 1     | Output    | Serial output of shadow register, MSB first. When SE is low, has the value of MSB of shadow register |
  *
  * @param width Word width input,output and registers
  * @param bld_shad_reg If one build both system register and shadow register
  */
class CW_shad_reg(val width: Int = 8, val bld_shad_reg: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "bld_shad_reg" -> bld_shad_reg
      )
    ) {
  require(width >= 1 && width <= 512, s"width must be in range [1, 512], but got $width")
  require(bld_shad_reg == 0 || bld_shad_reg == 1, s"bld_shad_reg must be either 0 or 1, but got $bld_shad_reg")

  val io = IO(new Bundle {
    val datain:   UInt  = Input(UInt(width.W))
    val sys_clk:  Clock = Input(Clock())
    val shad_clk: Clock = Input(Clock())
    val reset:    Bool  = Input(Bool())
    val SI:       Bool  = Input(Bool())
    val SE:       Bool  = Input(Bool())
    val sys_out:  UInt  = Output(UInt(width.W))
    val shad_out: UInt  = Output(UInt(width.W))
    val SO:       Bool  = Output(Bool())
  })
}
