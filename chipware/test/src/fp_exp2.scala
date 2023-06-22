import chisel3._

class fp_exp2(val sig_width: Int = 23,
              val exp_width: Int = 8,
              val ieee_compliance: Int = 1,
              val arch: Int = 0) extends Module {
  // Define an instance of the BlackBox class
  protected val U1: CW_fp_exp2 = Module(new CW_fp_exp2(sig_width, exp_width, ieee_compliance, arch))

  // Define ports matching the BlackBox class
  val io = IO(new Bundle {
    val a: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val status: UInt = Output(UInt(8.W))
    val z: UInt = Output(UInt((sig_width + exp_width + 1).W))
  })

  // Connect the module ports to the BlackBox ports
  U1.io.a := io.a
  io.status := U1.io.status
  io.z := U1.io.z
}
