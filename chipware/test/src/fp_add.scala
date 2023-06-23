import chisel3._

class fp_add(
  sig_width:           Int = 23,
  exp_width:           Int = 8,
  ieee_compliance:     Int = 1,
  ieee_NaN_compliance: Int = 0,
  arch:                Int = 0)
    extends RawModule {
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b:      UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd:    UInt = Input(UInt(3.W))
    val z:      UInt = Output(UInt((sig_width + exp_width + 1).W))
    val status: UInt = Output(UInt(8.W))
  })

  protected val U1: CW_fp_add = Module(
    new CW_fp_add(
      sig_width           = sig_width,
      exp_width           = exp_width,
      ieee_compliance     = ieee_compliance,
      ieee_NaN_compliance = ieee_NaN_compliance,
      arch                = arch
    )
  )

  U1.io.a   := io.a
  U1.io.b   := io.b
  U1.io.rnd := io.rnd
  io.z      := U1.io.z
  io.status := U1.io.status
}
