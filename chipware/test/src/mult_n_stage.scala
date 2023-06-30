import chisel3._

class mult_n_stage(val wA: Int = 8, val wB: Int = 8, val stages: Int = 2) extends RawModule {
  protected val U1: CW_mult_n_stage = Module(new CW_mult_n_stage(wA, wB, stages))

  val io = IO(new Bundle {
    val A:   UInt  = Input(UInt(wA.W))
    val B:   UInt  = Input(UInt(wB.W))
    val TC:  Bool  = Input(Bool())
    val CLK: Clock = Input(Clock())
    val Z:   UInt  = Output(UInt((wA + wB - 1).W))
  })

  U1.io.A   := io.A
  U1.io.B   := io.B
  U1.io.TC  := io.TC
  U1.io.CLK := io.CLK
  io.Z      := U1.io.Z
}
