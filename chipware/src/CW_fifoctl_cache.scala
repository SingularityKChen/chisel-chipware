// filename: CW_fifoctl_cache.scala
import chisel3._
import chisel3.experimental._

/**
  * == CW_fifoctl_cache ==
  *
  * === Abstract ===
  *
  * A pre fetch cache intended for CW_fifoctl_XX_YY series of devices.
  *
  * === Parameters ===
  *
  * | Parameter        | Legal Range | Default | Description                   |
  * |------------------|-------------|---------|-------------------------------|
  * | width            |             | 8       | width of data interface (>=1) |
  * | cache_depth      |             | 3       | depth of cache (>=1)          |
  *
  * === Ports ===
  *
  * | Pin Name          | Width | Direction | Description                           |
  * |-------------------|-------|-----------|---------------------------------------|
  * | rst_d_n           | 1 bit | input     | destination asynchronous reset        |
  * | init_d_n          | 1 bit | input     | destination synchronous reset         |
  * | clk_d             | 1 bit | input     | destination clock                     |
  * | user_pop_d_n      | 1 bit | input     | destination user (read) pop request    |
  * | data_d            | width | output    | destination pop data                  |
  * | cache_pop_d_n     | 1 bit | output    | cache pop_req_n -> destination ptr_gen|
  * | rd_data_d         | width | input     | ram data interface                    |
  * | flagRNE           | 1 bit | input     | flag - RAM not Empty                  |
  * | flagRNEP          | 1 bit | input     | flag - RAM not Empty Pending          |
  * | cache_word_count_next |      | input     | unregistered number of word locations in the cache that are occupied |
  * | cache_encoded_word_count |    | output  | registered and encoded number of word locations in the cache that are occupied |
  *
  * @param width              width of data interface (>=1)
  * @param cache_depth        depth of cache (>=1)
  */
class CW_fifoctl_cache(val width: Int = 8, val cache_depth: Int = 3)
    extends BlackBox(
      Map(
        "width" -> width,
        "cache_depth" -> cache_depth
      )
    ) {
  // Validation of all parameters
  require(width >= 1, "width must be >= 1")
  require(cache_depth >= 1, "cache_depth must be >= 1")
  protected val word_count_width: Int = if (cache_depth > 1) 2 else 1

  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_d_n:               Bool  = Input(Bool()) // destination asynchronous reset
    val init_d_n:              Bool  = Input(Bool()) // destination synchronous reset
    val clk_d:                 Clock = Input(Clock()) // destination clock
    val user_pop_d_n:          Bool  = Input(Bool()) // destination user (read) pop request
    val data_d:                UInt  = Output(UInt(width.W)) // destination pop data
    val cache_pop_d_n:         Bool  = Output(Bool()) // cache pop_req_n -> destination ptr_gen
    val rd_data_d:             UInt  = Input(UInt(width.W)) // ram data interface
    val flagRNE:               Bool  = Input(Bool()) // flag - RAM not Empty
    val flagRNEP:              Bool  = Input(Bool()) // flag - RAM not Empty Pending
    val cache_word_count_next: UInt  = Input(UInt(word_count_width.W)) // unregistered number of word locations
    val cache_encoded_word_count: UInt =
      Output(UInt(word_count_width.W)) // registered and encoded number of word locations
  })
}
