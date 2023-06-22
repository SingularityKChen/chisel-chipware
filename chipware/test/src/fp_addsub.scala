import chisel3._

class fp_addsub(
                 sig_width: Int = 23,
                 exp_width: Int = 8,
                 ieee_compliance: Int = 1
               ) extends Module {
  val io = IO(new Bundle {
    val a: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b: UInt = Input(UInt((sig_width + exp_width + 1).W))
    val rnd: UInt = Input(UInt(3.W))
    val op: Bool = Input(Bool())
    val z: UInt = Output(UInt((sig_width + exp_width + 1).W))
    val status: UInt = Output(UInt(8.W))
  })

  protected val U1: CW_fp_addsub = Module(new CW_fp_addsub(
    sig_width = sig_width,
    exp_width = exp_width,
    ieee_compliance = ieee_compliance
  ))

  U1.io.a := io.a
  U1.io.b := io.b
  U1.io.rnd := io.rnd
  U1.io.op := io.op
  io.z := U1.io.z
  io.status := U1.io.status
}
