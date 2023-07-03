import chisel3._
import chisel3.util._
import chisel3.experimental._

/**
  * CW_fifoctl_s2_sf
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | depth  | 4 to 2<sup>24</sup>  | 8 | Number of words that can be stored in FIFO |
  * | push_ae_lvl  | 1 to depth-1  | 2 | Almost empty level for push_ae output port |
  * | push_af_lvl  | 1 to depth-1  | 2 | Almost full level for the push_af output port |
  * | pop_ae_lvl  | 1 to depth-1  | 2 | Almost empty level for the pop_ae output port |
  * | pop_af_lvl  | 1 to depth-1  | 2 | Almost full level for the pop_af output port |
  * | err_mode  | 0 or 1  | 0 | Error mode |
  * | push_sync  | 0 to 4  | 2 | Push flag synchronization mode |
  * | pop_sync  | 0 to 4  | 2 | Pop flag synchronization mode |
  * | rst_mode  | 0 or 1  | 0 | Reset mode |
  * | tst_mode  | 0 or 1  | 0 | Test Mode |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | clk_push  | 1  | Input  | Input clock for push interface |
  * | clk_pop  | 1  | Input  | Input clock for pop interface |
  * | rst_n  | 1  | Input  | Reset input, active low |
  * | push_req_n  | 1  | Input  | FIFO push request, active low |
  * | pop_req_n  | 1  | Input  | FIFO pop request, active low |
  * | we_n  | 1  | Output  | Write enable output for write port of RAM, active low |
  * | push_empty  | 1  | Output  | FIFO empty output flag synchronous to clk_push, active low |
  * | push_ae  | 1  | Output  | FIFO almost empty output flag synchronous to clk_push, active high |
  * | push_hf  | 1  | Output  | FIFO half full output flag synchronous to clk_push,active high |
  * | push_af  | 1  | Output  | FIFO almost full output flag synchronous to clk_push, active high |
  * | push_full  | 1  | Output  | FIFO full output flag synchronous to clk_push, active high |
  * | push_error  | 1  | Output  | FIFO push error (overrun) output flag synchronous to clk_push, active high |
  * | pop_empty  | 1  | Output  | FIFO empty output flag synchronous to clk_pop, active high |
  * | pop_ae  | 1  | Output  | FIFO almost empty output flag synchronous to clk_pop, active high |
  * | pop_hf  | 1  | Output  | FIFO half full output flag synchronous to clk_pop, active high |
  * | pop_af  | 1  | Output  | FIFO almost full output flag synchronous to clk_pop, active high |
  * | pop_full  | 1  | Output  | FIFO full output flag synchronous to clk_pop, active high |
  * | pop_error  | 1  | Output  | FIFO pop error (underrun) output flag synchronous to clk_pop, active high |
  * | wr_addr  | ceil(log2[depth])  | Output  | Address output to write port of RAM |
  * | rd_addr  | ceil(log2[depth])  | Output  | Address output to read port of RAM |
  * | push_word_count | ceil(log2[depth+1])  | Output  | Words in FIFO |
  * | pop_word_count  | ceil(log2[depth])  | Output  | Words in FIFO |
  * | test  | 1  | Input  | Active high, test input control for inserting scan test look-up latches |
  *
  * @param depth Number of words that can be stored in FIFO
  * @param push_ae_lvl Almost empty level for push_ae output port
  * @param push_af_lvl Almost full level for the push_af output port
  * @param pop_ae_lvl Almost empty level for the pop_ae output port
  * @param pop_af_lvl Almost full level for the pop_af output port
  * @param err_mode Error mode
  * @param push_sync Push flag synchronization mode
  * @param pop_sync Pop flag synchronization mode
  * @param rst_mode Reset mode
  * @param tst_mode Test Mode
  */
class CW_fifoctl_s2_sf(
  val depth:       Int = 8,
  val push_ae_lvl: Int = 2,
  val push_af_lvl: Int = 2,
  val pop_ae_lvl:  Int = 2,
  val pop_af_lvl:  Int = 2,
  val err_mode:    Int = 0,
  val push_sync:   Int = 2,
  val pop_sync:    Int = 2,
  val rst_mode:    Int = 0,
  val tst_mode:    Int = 0)
    extends BlackBox(
      Map(
        "depth" -> depth,
        "push_ae_lvl" -> push_ae_lvl,
        "push_af_lvl" -> push_af_lvl,
        "pop_ae_lvl" -> pop_ae_lvl,
        "pop_af_lvl" -> pop_af_lvl,
        "err_mode" -> err_mode,
        "push_sync" -> push_sync,
        "pop_sync" -> pop_sync,
        "rst_mode" -> rst_mode,
        "tst_mode" -> tst_mode
      )
    ) {
  require(depth >= 4 && depth <= 16777216, s"depth must be >= 4 and <= 16777216, but got $depth")
  require(
    push_ae_lvl >= 1 && push_ae_lvl <= depth - 1,
    s"push_ae_lvl must be >= 1 and <= ${depth - 1}, but got $push_ae_lvl"
  )
  require(
    push_af_lvl >= 1 && push_af_lvl <= depth - 1,
    s"push_af_lvl must be >= 1 and <= ${depth - 1}, but got $push_af_lvl"
  )
  require(
    pop_ae_lvl >= 1 && pop_ae_lvl <= depth - 1,
    s"pop_ae_lvl must be >= 1 and <= ${depth - 1}, but got $pop_ae_lvl"
  )
  require(
    pop_af_lvl >= 1 && pop_af_lvl <= depth - 1,
    s"pop_af_lvl must be >= 1 and <= ${depth - 1}, but got $pop_af_lvl"
  )
  require(err_mode >= 0 && err_mode <= 1, s"err_mode must be 0 or 1, but got $err_mode")
  require(push_sync >= 0 && push_sync <= 4, s"push_sync must be >= 0 and <= 4, but got $push_sync")
  require(pop_sync >= 0 && pop_sync <= 4, s"pop_sync must be >= 0 and <= 4, but got $pop_sync")
  require(rst_mode >= 0 && rst_mode <= 1, s"rst_mode must be 0 or 1, but got $rst_mode")
  require(tst_mode >= 0 && tst_mode <= 1, s"tst_mode must be 0 or 1, but got $tst_mode")

  val io = IO(new Bundle {
    val clk_push:        Clock = Input(Clock())
    val clk_pop:         Clock = Input(Clock())
    val rst_n:           Bool  = Input(Bool())
    val push_req_n:      Bool  = Input(Bool())
    val pop_req_n:       Bool  = Input(Bool())
    val test:            Bool  = Input(Bool())
    val we_n:            Bool  = Output(Bool())
    val push_empty:      Bool  = Output(Bool())
    val push_ae:         Bool  = Output(Bool())
    val push_hf:         Bool  = Output(Bool())
    val push_af:         Bool  = Output(Bool())
    val push_full:       Bool  = Output(Bool())
    val push_error:      Bool  = Output(Bool())
    val pop_empty:       Bool  = Output(Bool())
    val pop_ae:          Bool  = Output(Bool())
    val pop_hf:          Bool  = Output(Bool())
    val pop_af:          Bool  = Output(Bool())
    val pop_full:        Bool  = Output(Bool())
    val pop_error:       Bool  = Output(Bool())
    val wr_addr:         UInt  = Output(UInt(log2Ceil(depth).W))
    val rd_addr:         UInt  = Output(UInt(log2Ceil(depth).W))
    val push_word_count: UInt  = Output(UInt((log2Ceil(depth + 1)).W))
    val pop_word_count:  UInt  = Output(UInt(log2Ceil(depth).W))
  })
}
