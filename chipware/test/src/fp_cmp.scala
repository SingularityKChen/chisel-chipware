import chisel3._

class fp_cmp(val sig_width: Int = 23, val exp_width: Int = 8, val ieee_compliance: Int = 1, val quieten_nans: Int = 1)
    extends RawModule {
  require(sig_width >= 2 && sig_width <= 253, "sig_width must be between 2 and 253")
  require(exp_width >= 3 && exp_width <= 31, "exp_width must be between 3 and 31")
  require(ieee_compliance == 0 || ieee_compliance == 1, "ieee_compliance must be 0 or 1")
  require(quieten_nans == 1, "quieten_nans must be 1")
  val io = IO(new Bundle {
    val a:         UInt = Input(UInt((sig_width + exp_width + 1).W))
    val b:         UInt = Input(UInt((sig_width + exp_width + 1).W))
    val status0:   UInt = Output(UInt(8.W))
    val status1:   UInt = Output(UInt(8.W))
    val z0:        UInt = Output(UInt((sig_width + exp_width + 1).W))
    val z1:        UInt = Output(UInt((sig_width + exp_width + 1).W))
    val zctr:      Bool = Input(Bool())
    val agtb:      Bool = Output(Bool())
    val altb:      Bool = Output(Bool())
    val aeqb:      Bool = Output(Bool())
    val unordered: Bool = Output(Bool())
  })

  // Instantiate the BlackBox
  protected val U1: CW_fp_cmp = Module(new CW_fp_cmp(sig_width, exp_width, ieee_compliance, quieten_nans))

  // Connect the ports
  U1.io.a      := io.a
  U1.io.b      := io.b
  io.status0   := U1.io.status0
  io.status1   := U1.io.status1
  io.z0        := U1.io.z0
  io.z1        := U1.io.z1
  U1.io.zctr   := io.zctr
  io.agtb      := U1.io.agtb
  io.altb      := U1.io.altb
  io.aeqb      := U1.io.aeqb
  io.unordered := U1.io.unordered
}
