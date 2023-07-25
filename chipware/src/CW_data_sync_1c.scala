import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_data_sync_1c ==
  *
  * === Abstract ===
  *
  * Single clock filtered data bus synchronizer
  *
  * === Parameters ===
  *
  * | Parameter    | Legal Range       | Default | Description                                      |
  * |--------------|-------------------|---------|--------------------------------------------------|
  * | width        | 1 to 1024          | 8       | Width of input data_s and output data_d         |
  * | f_sync_type  | 0 to 4             | 2       | Filter synchronization type                     |
  * | filt_size    | 1 to 8             | 1       | filt_d vector size                               |
  * | tst_mode     | 0 to 1             | 0       | Test mode                                        |
  * | verif_en     | 0 to 4             | 1       | Verification enable                              |
  *
  * === Ports ===
  *
  * | Name         | Width             | Direction | Description                                      |
  * |--------------|-------------------|-----------|--------------------------------------------------|
  * | data_s       | width bit(s)      | Input     | Source domain data                               |
  * | clk_d        | 1 bit             | Input     | Destination Domain Clock                         |
  * | rst_d_n      | 1 bit             | Input     | Destination clock domain Reset, asynchronous, active low |
  * | init_d_n     | 1 bit             | Input     | Destination clock domain Reset, synchronous, active low |
  * | filt_d       | filt_size bits(s) | Input     | Destination domain filter time                   |
  * | test         | 1 bit             | Input     | Scan test mode select                            |
  * | data_avail_d | 1 bit             | Output    | Destination domain data valid                    |
  * | data_d       | width bit(s)      | Output    | Destination domain synchronized data            |
  * | max_skew_d   | filt_size+1 bit(s)| Output    | Destination domain max skew detector for data_s bus transition |
  *
  * @param width       Width of input data_s and output data_d
  * @param f_sync_type Filter synchronization type
  * @param filt_size   filt_d vector size
  * @param tst_mode    Test mode
  * @param verif_en    Verification enable
  */
class CW_data_sync_1c(
  val width:       Int = 8,
  val f_sync_type: Int = 2,
  val filt_size:   Int = 1,
  val tst_mode:    Int = 0,
  val verif_en:    Int = 1)
    extends BlackBox(
      Map(
        "width" -> width,
        "f_sync_type" -> f_sync_type,
        "filt_size" -> filt_size,
        "tst_mode" -> tst_mode,
        "verif_en" -> verif_en
      )
    )
    with HasBlackBoxPath {
  // Validation of all parameters
  require(width >= 1 && width <= 1024, "width must be in the range [1, 1024]")
  require(f_sync_type >= 0 && f_sync_type <= 4, "f_sync_type must be in the range [0, 4]")
  require(filt_size >= 1 && filt_size <= 8, "filt_size must be in the range [1, 8]")
  require(tst_mode >= 0 && tst_mode <= 1, "tst_mode must be in the range [0, 1]")
  require(verif_en >= 0 && verif_en <= 4, "verif_en must be in the range [0, 4]")

  // Define ports with type annotations
  val io = IO(new Bundle {
    val data_s:       UInt  = Input(UInt(width.W))
    val clk_d:        Clock = Input(Clock())
    val rst_d_n:      Bool  = Input(Bool())
    val init_d_n:     Bool  = Input(Bool())
    val filt_d:       UInt  = Input(UInt(filt_size.W))
    val test:         Bool  = Input(Bool())
    val data_avail_d: Bool  = Output(Bool())
    val data_d:       UInt  = Output(UInt(width.W))
    val max_skew_d:   UInt  = Output(UInt((filt_size + 1).W))
  })
}
