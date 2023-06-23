import chisel3._

class fp_sincos(
                 sig_width: Int = 33,
                 exp_width: Int = 6,
                 ieee_compliance: Int = 1,
                 pi_multiple: Int = 1
               ) extends RawModule {
  val io = IO(new Bundle {
    val a: UInt = Input(UInt((1 + exp_width + sig_width).W))
    val sin_cos: Bool = Input(Bool())
    val z: UInt = Output(UInt((1 + exp_width + sig_width).W))
    val status: UInt = Output(UInt(8.W))
  })

  val U1: CW_fp_sincos = Module(new CW_fp_sincos(sig_width, exp_width, ieee_compliance, pi_multiple))
  U1.io.a := io.a
  U1.io.sin_cos := io.sin_cos
  io.z := U1.io.z
  io.status := U1.io.status
}
