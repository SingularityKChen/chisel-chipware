import chisel3._

class bc_4 extends RawModule {
  // Define ports with type annotations
  val io = IO(new Bundle {
    val capture_clk: Clock = Input(Clock())
    val capture_en:  Bool  = Input(Bool())
    val shift_dr:    Bool  = Input(Bool())
    val si:          Bool  = Input(Bool())
    val data_in:     Bool  = Input(Bool())
    val so:          Bool  = Output(Bool())
    val data_out:    Bool  = Output(Bool())
  })

  // Define the submodule declaration
  protected val U1: CW_bc_4 = Module(new CW_bc_4())

  // Connect ports between the modules
  U1.io.capture_clk := io.capture_clk
  U1.io.capture_en  := io.capture_en
  U1.io.shift_dr    := io.shift_dr
  U1.io.si          := io.si
  U1.io.data_in     := io.data_in
  io.so             := U1.io.so
  io.data_out       := U1.io.data_out
}
