import chisel3._
import chisel3.experimental._
import chisel3.util._

/**
  * == CW_fifo_s2_sf ==
  *
  * === Abstract ===
  *
  * Synchronous (Dual-Clock) FIFO with Static Flags
  *
  * === Parameters ===
  *
  * | Parameter     | Values             | Description                                                                                       |
  * |---------------|--------------------|---------------------------------------------------------------------------------------------------|
  * | width         | 1 to 256           | Width of the data_in and data_out buses                                                           |
  * | depth         | 4 to 256           | Number of words that can be stored in FIFO                                                         |
  * | push_ae_lvl   | 1 to depth-1       | Almost empty level for the push_ae output port                                                     |
  * | push_af_lvl   | 1 to depth-1       | Almost full level for the push_af output port                                                      |
  * | pop_ae_lvl    | 1 to depth-1       | Almost empty level for the pop_ae output port                                                      |
  * | pop_af_lvl    | 1 to depth-1       | Almost full level for the pop_af output port                                                       |
  * | err_mode      | 0 or 1             | Error mode                                                                                         |
  * | push_sync     | 0 to 4             | Push flag synchronization mode                                                                     |
  * | pop_sync      | 0 to 4             | Pop flag synchronization mode                                                                      |
  * | rst_mode      | 0 to 3             | Reset mode                                                                                         |
  *
  * === Ports ===
  *
  * | Name         | Width             | Direction | Description                         |
  * |--------------|-------------------|-----------|-------------------------------------|
  * | clk_push     | 1 bit             | Input     | Input clock for push interface       |
  * | clk_pop      | 1 bit             | Input     | Input clock for pop interface        |
  * | rst_n        | 1 bit             | Input     | Reset input, active low              |
  * | push_req_n   | 1 bit             | Input     | FIFO push request, active low        |
  * | pop_req_n    | 1 bit             | Input     | FIFO pop request, active low         |
  * | data_in      | width bit(s)      | Input     | FIFO data to push                    |
  * | push_empty   | 1 bit             | Output    | FIFO empty a output flag             |
  * | push_ae      | 1 bit             | Output    | FIFO almost emptya output flag       |
  * | push_hf      | 1 bit             | Output    | FIFO half fulla output flag          |
  * | push_af      | 1 bit             | Output    | FIFO almost fulla output flag        |
  * | push_full    | 1 bit             | Output    | FIFO full output flag                |
  * | push_error   | 1 bit             | Output    | FIFO push error (overrun) output flag |
  * | pop_empty    | 1 bit             | Output    | FIFO empty output flag               |
  * | pop_ae       | 1 bit             | Output    | FIFO almost emptyb output flag       |
  * | pop_hf       | 1 bit             | Output    | FIFO half fullb output flag          |
  * | pop_af       | 1 bit             | Output    | FIFO almost fullb output flag        |
  * | pop_full     | 1 bit             | Output    | FIFO full output flag                |
  * | pop_error    | 1 bit             | Output    | FIFO pop error (underrun) output flag |
  * | data_out     | width bit(s)      | Output    | FIFO data to pop                     |
  *
  * @param width       Width of the data_in and data_out buses
  * @param depth       Number of words that can be stored in FIFO
  * @param push_ae_lvl Almost empty level for the push_ae output port
  * @param push_af_lvl Almost full level for the push_af output port
  * @param pop_ae_lvl  Almost empty level for the pop_ae output port
  * @param pop_af_lvl  Almost full level for the pop_af output port
  * @param err_mode    Error mode
  * @param push_sync   Push flag synchronization mode
  * @param pop_sync    Pop flag synchronization mode
  * @param rst_mode    Reset mode
  */
class CW_fifo_s2_sf(
  val width:       Int,
  val depth:       Int,
  val push_ae_lvl: Int,
  val push_af_lvl: Int,
  val pop_ae_lvl:  Int,
  val pop_af_lvl:  Int,
  val err_mode:    Int,
  val push_sync:   Int,
  val pop_sync:    Int,
  val rst_mode:    Int)
    extends BlackBox(
      Map(
        "width" -> width,
        "depth" -> depth,
        "push_ae_lvl" -> push_ae_lvl,
        "push_af_lvl" -> push_af_lvl,
        "pop_ae_lvl" -> pop_ae_lvl,
        "pop_af_lvl" -> pop_af_lvl,
        "err_mode" -> err_mode,
        "push_sync" -> push_sync,
        "pop_sync" -> pop_sync,
        "rst_mode" -> rst_mode
      )
    )
    with HasBlackBoxResource {

  val io = IO(new Bundle {
    val clk_push:   Clock = Input(Clock())
    val clk_pop:    Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val push_req_n: Bool  = Input(Bool())
    val pop_req_n:  Bool  = Input(Bool())
    val data_in:    UInt  = Input(UInt(width.W))
    val push_empty: Bool  = Output(Bool())
    val push_ae:    Bool  = Output(Bool())
    val push_hf:    Bool  = Output(Bool())
    val push_af:    Bool  = Output(Bool())
    val push_full:  Bool  = Output(Bool())
    val push_error: Bool  = Output(Bool())
    val pop_empty:  Bool  = Output(Bool())
    val pop_ae:     Bool  = Output(Bool())
    val pop_hf:     Bool  = Output(Bool())
    val pop_af:     Bool  = Output(Bool())
    val pop_full:   Bool  = Output(Bool())
    val pop_error:  Bool  = Output(Bool())
    val data_out:   UInt  = Output(UInt(width.W))
  })

}
