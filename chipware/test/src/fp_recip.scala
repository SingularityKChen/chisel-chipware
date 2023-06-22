import chisel3._

class fp_recip(val sig_width: Int = 23,
               val exp_width: Int = 8,
               val ieee_compliance: Int = 1,
               val faithful_round: Int = 0,
               val arch: Int = 0) extends Module {
  val io = IO(new Bundle {
    val a: UInt = Input(UInt((sig_width + exp_width).W))
    val rnd: UInt = Input(UInt(3.W))
    val status: UInt = Output(UInt(8.W))
    val z: UInt = Output(UInt((sig_width + exp_width).W))
  })
  protected val U1: CW_fp_recip = Module(new CW_fp_recip(sig_width, exp_width, ieee_compliance, faithful_round, arch))

  U1.io.a := io.a
  U1.io.rnd := io.rnd
  io.status := U1.io.status
  io.z := U1.io.z
}