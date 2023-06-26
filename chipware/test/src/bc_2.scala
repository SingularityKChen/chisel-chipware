import chisel3._

class bc_2 extends RawModule {
  val io = IO(new Bundle {
    val capture_clk: Clock = Input(Clock())
    val update_clk:  Clock = Input(Clock())
    val capture_en:  Bool  = Input(Bool())
    val update_en:   Bool  = Input(Bool())
    val shift_dr:    Bool  = Input(Bool())
    val mode:        Bool  = Input(Bool())
    val si:          Bool  = Input(Bool())
    val data_in:     Bool  = Input(Bool())
    val data_out:    Bool  = Output(Bool())
    val so:          Bool  = Output(Bool())
  })
  // Instantiate the blackbox module
  protected val U1: CW_bc_2 = Module(new CW_bc_2())

  // Connect the ports between the raw module and the blackbox module
  U1.io.capture_clk := io.capture_clk
  U1.io.update_clk  := io.update_clk
  U1.io.capture_en  := io.capture_en
  U1.io.update_en   := io.update_en
  U1.io.shift_dr    := io.shift_dr
  U1.io.mode        := io.mode
  U1.io.si          := io.si
  U1.io.data_in     := io.data_in
  io.data_out       := U1.io.data_out
  io.so             := U1.io.so
}
