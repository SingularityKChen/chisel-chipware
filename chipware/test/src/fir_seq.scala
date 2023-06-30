import chisel3._

class fir_seq(val data_in_width: Int = 8,
              val coef_width: Int = 8,
              val data_out_width: Int = 16,
              val order: Int = 6) extends RawModule {
  val io = IO(new Bundle {
    val clk: Clock = Input(Clock())
    val rst_n: Bool = Input(Bool())
    val coef_shift_en: Bool = Input(Bool())
    val tc: Bool = Input(Bool())
    val run: Bool = Input(Bool())
    val data_in: UInt = Input(UInt(data_in_width.W))
    val coef_in: UInt = Input(UInt(coef_width.W))
    val init_acc_val: UInt = Input(UInt(data_out_width.W))
    val start: Bool = Output(Bool())
    val hold: Bool = Output(Bool())
    val data_out: UInt = Output(UInt(data_out_width.W))
  })

  protected val U1: CW_fir_seq = Module(new CW_fir_seq(data_in_width, coef_width, data_out_width, order))
  U1.io.clk := io.clk
  U1.io.rst_n := io.rst_n
  U1.io.coef_shift_en := io.coef_shift_en
  U1.io.tc := io.tc
  U1.io.run := io.run
  U1.io.data_in := io.data_in
  U1.io.coef_in := io.coef_in
  U1.io.init_acc_val := io.init_acc_val
  io.start := U1.io.start
  io.hold := U1.io.hold
  io.data_out := U1.io.data_out
}

