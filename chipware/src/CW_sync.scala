import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_sync ==
  *
  * === Abstract ===
  *
  * Multi-bit Meta-Stability Hardening Circuit.
  * CW_sync is designed for the synchronization of data bus crossing asynchronous clock boundaries.
  * It is configurable for number synchronizing stages (2,3,or 4)
  * and can inserts 'hold latch' to faciliate the scan testing.
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Range  | Default  | Description  |
  * |--------------|--------------|----------|--------------|
  * | width        | 1 to 1024    | 8        | Bit width of input data_s and output data_d. |
  * | f_sync_type  | 0 to 4       | 2        | Defines the type of register and the number of stages in synchronization. <br> 0: Single clock design, no synchronizer and register inferred. <br> 1: 2 stage synchronizer inferred. 1st stage is a negative edge register and 2nd stage is a positive edge register. <br> 2: 2 stage synchronizer inferred. All stages are positive edge registers. <br> 3: 3 stage synchronizer inferred. All stages are positive edge registers. <br> 4: 4 stage synchronizer inferred. <br> All stages are positive edge registers. |
  * | tst_mode     | 0 to 1       | 0        | Insert the negative edge scan register. <br> 0: No scan register is inferred <br> 1: Negative edge scan register inferred. |
  * | verif_en     | 0            | 0        | verification enable |
  *
  * === Ports ===
  *
  * | Name     | Width  | Direction | Description  |
  * |----------|--------|-----------|--------------|
  * | data_s   | width  | input     | Asynchronous incoming data. |
  * | clk_d    | 1 bit  | input     | Destination clock on which incoming data is synchronized. |
  * | rst_d_n  | 1 bit  | input     | Destination domain asynchronous reset pin (active low). |
  * | init_d_n | 1 bit  | input     | Destination domain synchronous reset pin (active low). |
  * | test     | 1 bit  | input     | dft scan test mode select. <br> 0: No scan register inferred. <br> 1: Negative edge scan register is inferred (before synchronizer). |
  * | data_d   | width  | output    | Data synchronized with the clock domain clk_d. |
  *
  * @param width       Bit width of input data_s and output data_d.
  * @param f_sync_type synchronization type
  * @param tst_mode    test mode
  * @param verif_en    verification enable
  */
class CW_sync(val width: Int = 8, val f_sync_type: Int = 2, val tst_mode: Int = 0, val verif_en: Int = 0)
    extends BlackBox(
      Map(
        "width" -> width,
        "f_sync_type" -> f_sync_type,
        "tst_mode" -> tst_mode,
        "verif_en" -> verif_en
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 1 && width <= 1024, "width must be in the range [1, 1024]")
  require(f_sync_type >= 0 && f_sync_type <= 4, "f_sync_type must be in the range [0, 4]")
  require(tst_mode >= 0 && tst_mode <= 1, "tst_mode must be in the range [0, 1]")
  require(verif_en == 0, "verif_en must be 0")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val data_s:   UInt  = Input(UInt(width.W))
    val clk_d:    Clock = Input(Clock())
    val rst_d_n:  Bool  = Input(Bool())
    val init_d_n: Bool  = Input(Bool())
    val test:     Bool  = Input(Bool())
    val data_d:   UInt  = Output(UInt(width.W))
  })
}
