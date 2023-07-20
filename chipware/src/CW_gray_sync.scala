import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_gray_sync ==
  *
  * === Abstract ===
  *
  * Gray Code Synchornizer
  *
  * === Parameters ===
  *
  * | Parameter          | Legal Range         | Default | Description                                      |
  * |--------------------|---------------------|---------|--------------------------------------------------|
  * | width              | 1 to 1024           | 8       | Width of input data_s and output data_d         |
  * | offset             | 0 to 2<sup>(width-1)</sup>-1  | 0       | Offset value                                     |
  * | reg_count_d        | 0 to 1              | 1       | count_d registered or non registered             |
  * | f_sync_type        | 0 to 4              | 2       | Synchronizer type                                |
  * | tst_mode           | 0 to 1              | 0       | Test mode                                        |
  * | verif_en           | 0 to 4              | 0       | Verification enable                              |
  * | pipe_delay         | 0 to 2              | 0       | Pipeline stage added in Binary to Gray code results |
  * | reg_count_s        | 0 to 1              | 1       | count_s registered or non registered             |
  * | reg_offset_count_s | 0 to 1              | 1       | offset_count_s registered or non registered      |
  *
  * === Ports ===
  *
  * | Name          | Width      | Direction | Description                                      |
  * |---------------|------------|-----------|--------------------------------------------------|
  * | clk_s         | 1 bit      | Input     | Source Domain Clock                              |
  * | rst_s_n       | 1 bit      | Input     | Source clock domain Reset, asynchronous, active low |
  * | init_s_n      | 1 bit      | Input     | Source clock domain Reset, synchronous, active low |
  * | en_s          | 1 bit      | Input     | Source domain counter advance initiation         |
  * | count_s       | width bit(s) | Output  | Source domain counter value                      |
  * | offset_count_s | width bit(s) | Output  | Source domain offset counter value               |
  * | clk_d         | 1 bit      | Input     | Destination Domain Clock                         |
  * | rst_d_n       | 1 bit      | Input     | Destination clock domain Reset, asynchronous, active low |
  * | init_d_n      | 1 bit      | Input     | Destination clock domain Reset, synchronous, active low |
  * | count_d       | width bit(s) | Output  | Source domain counter value                      |
  * | test          | 1 bit      | Input     | Scan test mode select                            |
  *
  * @param width Width of input data_s and output data_d
  * @param offset Offset value
  * @param reg_count_d count_d registered or non registered
  * @param f_sync_type Synchronizer type
  * @param tst_mode Test mode
  * @param verif_en Verification enable
  * @param pipe_delay Pipeline stage added in Binary to Gray code results
  * @param reg_count_s count_s registered or non registered
  * @param reg_offset_count_s offset_count_s registered or non registered
  */
class CW_gray_sync(
  val width:              Int = 8,
  val offset:             Int = 0,
  val reg_count_d:        Int = 1,
  val f_sync_type:        Int = 2,
  val tst_mode:           Int = 0,
  val verif_en:           Int = 0,
  val pipe_delay:         Int = 0,
  val reg_count_s:        Int = 1,
  val reg_offset_count_s: Int = 1)
    extends BlackBox(
      Map(
        "width" -> width,
        "offset" -> offset,
        "reg_count_d" -> reg_count_d,
        "f_sync_type" -> f_sync_type,
        "tst_mode" -> tst_mode,
        "verif_en" -> verif_en,
        "pipe_delay" -> pipe_delay,
        "reg_count_s" -> reg_count_s,
        "reg_offset_count_s" -> reg_offset_count_s
      )
    )
    with HasBlackBoxPath {
  require(width >= 1 && width <= 1024, "width must be in the range [1, 1024]")
  require(offset >= 0 && offset <= ((1 << (width - 1)) - 1), "offset must be in the range [0, 2^(width-1)-1]")
  require(reg_count_d >= 0 && reg_count_d <= 1, "reg_count_d must be in the range [0, 1]")
  require(f_sync_type >= 0 && f_sync_type <= 4, "f_sync_type must be in the range [0, 4]")
  require(tst_mode >= 0 && tst_mode <= 1, "tst_mode must be in the range [0, 1]")
  require(verif_en >= 0 && verif_en <= 4, "verif_en must be in the range [0, 4]")
  require(pipe_delay >= 0 && pipe_delay <= 2, "pipe_delay must be in the range [0, 2]")
  require(reg_count_s >= 0 && reg_count_s <= 1, "reg_count_s must be in the range [0, 1]")
  require(reg_offset_count_s >= 0 && reg_offset_count_s <= 1, "reg_offset_count_s must be in the range [0, 1]")

  val io = IO(new Bundle {
    val clk_s:          Clock = Input(Clock())
    val rst_s_n:        Bool  = Input(Bool())
    val init_s_n:       Bool  = Input(Bool())
    val en_s:           Bool  = Input(Bool())
    val count_s:        UInt  = Output(UInt(width.W))
    val offset_count_s: UInt  = Output(UInt(width.W))
    val clk_d:          Clock = Input(Clock())
    val rst_d_n:        Bool  = Input(Bool())
    val init_d_n:       Bool  = Input(Bool())
    val count_d:        UInt  = Output(UInt(width.W))
    val test:           Bool  = Input(Bool())
  })
}
