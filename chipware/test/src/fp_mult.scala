import chisel3._

class fp_mult(val sig_width: Int = 23, val exp_width: Int = 8, val ieee_compliance: Int = 1, val arch: Int = 0) extends RawModule {
  val io = IO(new Bundle {
    val a: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd: UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z: UInt = Output(UInt((sig_width + exp_width + 1).W))
  })

  protected val U1: CW_fp_mult = Module(new CW_fp_mult(sig_width, exp_width, ieee_compliance, arch))
  U1.io.a := io.a
  U1.io.b := io.b
  U1.io.rnd := io.rnd
  io.status := U1.io.status
  io.z := U1.io.z
}
