import chisel3._

// Chisel Module class
class fp_div_seq(
  val sig_width:       Int = 23,
  val exp_width:       Int = 8,
  val ieee_compliance: Int = 0,
  val rst_mode:        Int = 0,
  val input_mode:      Int = 1,
  val output_mode:     Int = 1,
  val early_start:     Int = 0,
  val internal_reg:    Int = 1,
  val num_cyc:         Int = 5,
  val arch:            Int = 0)
    extends RawModule {
  // Create an instance of the BlackBox class
  protected val U1: CW_fp_div_seq = Module(
    new CW_fp_div_seq(
      sig_width,
      exp_width,
      ieee_compliance,
      rst_mode,
      input_mode,
      output_mode,
      early_start,
      internal_reg,
      num_cyc,
      arch
    )
  )

  // Define ports
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock())
    val rst_n:    Bool  = Input(Bool())
    val start:    Bool  = Input(Bool())
    val rnd:      Bool  = Input(Bool())
    val a:        UInt  = Input(UInt((sig_width + exp_width + 1).W))
    val b:        UInt  = Input(UInt((sig_width + exp_width + 1).W))
    val z:        UInt  = Output(UInt((sig_width + exp_width + 1).W))
    val status:   UInt  = Output(UInt(8.W))
    val complete: Bool  = Output(Bool())
  })

  // Connect the BlackBox instance ports to the Module ports
  U1.io.clk   := io.clk
  U1.io.rst_n := io.rst_n
  U1.io.start := io.start
  U1.io.rnd   := io.rnd
  U1.io.a     := io.a
  U1.io.b     := io.b
  io.z        := U1.io.z
  io.status   := U1.io.status
  io.complete := U1.io.complete
}
