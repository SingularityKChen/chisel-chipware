// filename: bc_8.scala
import chisel3._

class bc_8 extends RawModule {
  // Define ports with type annotations
  val io = IO(new Bundle {
    val capture_clk: Clock = Input(Clock())
    val update_clk:  Clock = Input(Clock())
    val capture_en:  Bool  = Input(Bool())
    val update_en:   Bool  = Input(Bool())
    val shift_dr:    Bool  = Input(Bool())
    val mode:        Bool  = Input(Bool())
    val si:          Bool  = Input(Bool())
    val pin_input:   Bool  = Input(Bool())
    val output_data: Bool  = Input(Bool())
    val ic_input:    Bool  = Output(Bool())
    val data_out:    Bool  = Output(Bool())
    val so:          Bool  = Output(Bool())
  })

  // Instantiate the previously defined Chisel BlackBox class
  protected val U1: CW_bc_8 = Module(new CW_bc_8())

  U1.io.capture_clk := io.capture_clk
  U1.io.update_clk  := io.update_clk
  U1.io.capture_en  := io.capture_en
  U1.io.update_en   := io.update_en
  U1.io.shift_dr    := io.shift_dr
  U1.io.mode        := io.mode
  U1.io.si          := io.si
  U1.io.pin_input   := io.pin_input
  U1.io.output_data := io.output_data
  io.ic_input       := U1.io.ic_input
  io.data_out       := U1.io.data_out
  io.so             := U1.io.so
}
