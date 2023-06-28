import chisel3._

class fp_i2flt(val sig_width: Int = 23, val exp_width: Int = 8, val isize: Int = 32, val isign: Int = 1)
    extends RawModule {
  val io = IO(new Bundle {
    val a:      UInt = Input(UInt(isize.W))
    val rnd:    UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z:      UInt = Output(UInt((sig_width + exp_width + 1).W))
  })

  protected val U1: CW_fp_i2flt = Module(new CW_fp_i2flt(sig_width, exp_width, isize, isign))

  U1.io.a   := io.a
  U1.io.rnd := io.rnd
  io.status := U1.io.status
  io.z      := U1.io.z
}
