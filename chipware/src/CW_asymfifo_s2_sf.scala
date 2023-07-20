// filename: CW_asymfifo_s2_sf.scala
import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_asymfifo_s2_sf ==
  *
  * === Abstract ===
  *
  * Asymmetric Synchronous (Dual-Clock) FIFO - Static Flags
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | data_in_width  | 1 to 256  | 8 | Width of the data_in bus. data_in_width must be an integer-multiple of data_out_width. That is, either data_in_width = K × data_out_width, or data_out_width = K × data_in_width, where K is an integer. |
  * | data_out_width  | 1 to 256  | 8 | Width of the data_out bus. data_out_width must be an integer-multiple of data_in_width. That is, either data_in_width = K × data_out_width, or data_out_width = K × data_in_width, where K is an integer. |
  * | depth  | 4 to 256  | 8 | Number of words that can be stored in FIFO |
  * | push_ae_lvl  | 1 to depth-1  | 2 | Almost empty level for the push_ae output port (the number of words in the FIFO at or below which the push_ae flag is active). |
  * | push_af_lvl  | 1 to depth-1  | 2 | Almost full level for the push_af output port (the number of empty memory locations in the FIFO at which the push_af flag is active.) |
  * | pop_ae_lvl  | 1 to depth-1  | 2 | Almost empty level for the pop_ae output port (the number of words in the FIFO at or below which the pop_ae flag is active) |
  * | pop_af_lvl  | 1 to depth-1  | 2 | Almost full level for the pop_af output port (the number of empty memory locations in the FIFO at which the pop_af flag is active.) |
  * | err_mode  | 0 or 1  | 0 | Error mode 0 = stays active until reset [latched], 1 = active only as long as error condition exists [unlatched] |
  * | push_sync  | 0 to 4  | 2 | Push flag synchronization mode Default: 2 0 = single clock design no synchronization mechanism is implemented 1 = 2-sample synchronizer 1st sample neg-edge 2nd sample pos-edge 2 = 2-sample synchronizer both sample pos-edge 3 = 3-stage synchronizer all samples pos-edge 4 = 4-stage synchronizer all samples pos-edge |
  * | pop_sync  | 0 to 4  | 2 | Pop flag synchronization mode  Default: 2 0 = single clock design no synchronization mechanism is implemented 1 = 2-sample synchronizer 1st sample neg-edge 2nd sample pos-edge 2 = 2-sample synchronizer both sample pos-edge 3 = 3-stage synchronizer all samples pos-edge 4 = 4-stage synchronizer all samples pos-edge |
  * | rst_mode  | 0 or 3  | 1 | Reset mode Default: 1 0 = asynchronous reset including memory, 1 = synchronous reset including memory, 2 = asynchronous reset excluding memory, 3 = synchronous reset excluding memory). |
  * | byte_order  | 0 or 1  | 0 | Order of bytes or subword within a word Default: 0 0 = first byte is in most significant bits position; 1 = first byte is in the least significant bits position. |
  * | ram_width  | -  | (data_in_width > data_out_width ? data_in_width : data_out_width) | - |
  * | ram_rst_mode  | -  | (rst_mode % 2) | - |
  * | ctl_rst_mode  | -  | (rst_mode % 2) | - |
  * | log_depth  | -  | log2(depth) | - |
  * | log_depth_plus1 | -  | log2(depth+1) | - |
  * | ram_depth  | -  | (log_depth_plus1 > log_depth) ? depth : ((depth % 2) ? (depth+1) : (depth+2)) | - |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | clk_push  | 1 bit  | Input | Input clock for push interface |
  * | clk_pop  | 1 bit  | Input | Input clock for pop interface |
  * | rst_n  | 1 bit  | Input | Reset input, active low |
  * | push_req_n  | 1 bit  | Input | FIFO push request, active low |
  * | flush_n  | 1 bit  | Input | Flushes the partial word into memory (fills in 0's for empty bits) (for data_in_width < data_out_width only), active low |
  * | pop_req_n  | 1 bit  | Input | FIFO pop request, active low |
  * | data_in  | data_in_width bit(s) | Input | FIFO data to push |
  * | push_empty  | 1 bit  | Output | FIFO emptya output flag synchronous to clk_push, active high |
  * | push_ae  | 1 bit  | Output | FIFO almost emptya output flag synchronous to clk_push (determined by push_ae_lvl parameter), active high |
  * | push_hf  | 1 bit  | Output | FIFO half fulla output flag synchronous to clk_push, active high |
  * | push_af  | 1 bit  | Output | FIFO almost fulla output flag synchronous to clk_push (determined by push_af_lvl parameter), active high |
  * | push_full  | 1 bit  | Output | FIFO's RAM fulla output flag (including the input buffer of FIFO for data_in_width < data_out_width) synchronous to clk_push, active high |
  * | ram_full  | 1 bit  | Output | FIFO's RAM (excluding the input buffer of FIFO for data_in_width < data_out_width) full output flag synchronous to clk_push, active high |
  * | part_wd  | 1 bit  | Output | Partial word accumulated in the input buffer synchronous to clk_push (for data_in_width < data_out_width only; otherwise, tied low), active high |
  * | push_error  | 1 bit  | Output | FIFO push error (overrun) output flag synchronous to clk_push, active high |
  * | pop_empty  | 1 bit  | Output | FIFO emptyb output flag synchronous to clk_pop, active high |
  * | pop_ae  | 1 bit  | Output | FIFO almost emptyb output flag synchronous to clk_pop (determined by pop_ae_lvl parameter),active high |
  * | pop_hf  | 1 bit  | Output | FIFO half fullb output flag synchronous to clk_pop, active high |
  * | pop_af  | 1 bit  | Output | FIFO almost fullb output flag synchronous to clk_pop (determined by pop_af_lvl parameter), active high |
  * | pop_full  | 1 bit  | Output | FIFO's RAM fullb output flag (excluding the input buffer of FIFO for case data_in_width < data_out_width) synchronous to clk_pop, active high |
  * | pop_error  | 1 bit  | Output | FIFO pop error (underrun) output flag synchronous to clk_pop, active high |
  * | data_out  | data_out_width bit(s) | Output | FIFO data to pop |
  *
  * @param data_in_width Width of the data_in bus. data_in_width must be an integer-multiple of data_out_width. That is, either data_in_width = K × data_out_width, or data_out_width = K × data_in_width, where K is an integer.
  * @param data_out_width Width of the data_out bus. data_out_width must be an integer-multiple of data_in_width. That is, either data_in_width = K × data_out_width, or data_out_width = K × data_in_width, where K is an integer.
  * @param depth Number of words that can be stored in FIFO
  * @param push_ae_lvl Almost empty level for the push_ae output port (the number of words in the FIFO at or below which the push_ae flag is active).
  * @param push_af_lvl Almost full level for the push_af output port (the number of empty memory locations in the FIFO at which the push_af flag is active.)
  * @param pop_ae_lvl Almost empty level for the pop_ae output port (the number of words in the FIFO at or below which the pop_ae flag is active)
  * @param pop_af_lvl Almost full level for the pop_af output port (the number of empty memory locations in the FIFO at which the pop_af flag is active.)
  * @param err_mode Error mode 0 = stays active until reset [latched], 1 = active only as long as error condition exists [unlatched]
  * @param push_sync Push flag synchronization mode Default: 2 0 = single clock design no synchronization mechanism is implemented 1 = 2-sample synchronizer 1st sample neg-edge 2nd sample pos-edge 2 = 2-sample synchronizer both sample pos-edge 3 = 3-stage synchronizer all samples pos-edge 4 = 4-stage synchronizer all samples pos-edge
  * @param pop_sync Pop flag synchronization mode  Default: 2 0 = single clock design no synchronization mechanism is implemented 1 = 2-sample synchronizer 1st sample neg-edge 2nd sample pos-edge 2 = 2-sample synchronizer both sample pos-edge 3 = 3-stage synchronizer all samples pos-edge 4 = 4-stage synchronizer all samples pos-edge
  * @param rst_mode Reset mode Default: 1 0 = asynchronous reset including memory, 1 = synchronous reset including memory, 2 = asynchronous reset excluding memory, 3 = synchronous reset excluding memory).
  * @param byte_order Order of bytes or subword within a word Default: 0 0 = first byte is in most significant bits position; 1 = first byte is in the least significant bits position.
  */
class CW_asymfifo_s2_sf(
  val data_in_width:  Int = 8,
  val data_out_width: Int = 8,
  val depth:          Int = 8,
  val push_ae_lvl:    Int = 2,
  val push_af_lvl:    Int = 2,
  val pop_ae_lvl:     Int = 2,
  val pop_af_lvl:     Int = 2,
  val err_mode:       Int = 0,
  val push_sync:      Int = 2,
  val pop_sync:       Int = 2,
  val rst_mode:       Int = 1,
  val byte_order:     Int = 0)
    extends BlackBox(
      Map(
        "data_in_width" -> data_in_width,
        "data_out_width" -> data_out_width,
        "depth" -> depth,
        "push_ae_lvl" -> push_ae_lvl,
        "push_af_lvl" -> push_af_lvl,
        "pop_ae_lvl" -> pop_ae_lvl,
        "pop_af_lvl" -> pop_af_lvl,
        "err_mode" -> err_mode,
        "push_sync" -> push_sync,
        "pop_sync" -> pop_sync,
        "rst_mode" -> rst_mode,
        "byte_order" -> byte_order
      )
    )
    with HasBlackBoxPath {
  require(data_in_width >= 1 && data_in_width <= 256, "data_in_width must be in range [1, 256]")
  require(data_out_width >= 1 && data_out_width <= 256, "data_out_width must be in range [1, 256]")
  require(depth >= 4 && depth <= 256, "depth must be in range [4, 256]")
  require(push_ae_lvl >= 1 && push_ae_lvl <= depth - 1, "push_ae_lvl must be in range [1, depth - 1]")
  require(push_af_lvl >= 1 && push_af_lvl <= depth - 1, "push_af_lvl must be in range [1, depth - 1]")
  require(pop_ae_lvl >= 1 && pop_ae_lvl <= depth - 1, "pop_ae_lvl must be in range [1, depth - 1]")
  require(pop_af_lvl >= 1 && pop_af_lvl <= depth - 1, "pop_af_lvl must be in range [1, depth - 1]")
  require(err_mode == 0 || err_mode == 1, "err_mode must be either 0 or 1")
  require(push_sync >= 0 && push_sync <= 4, "push_sync must be in range [0, 4]")
  require(pop_sync >= 0 && pop_sync <= 4, "pop_sync must be in range [0, 4]")
  require(rst_mode == 0 || rst_mode == 1 || rst_mode == 2 || rst_mode == 3, "rst_mode must be in range [0, 3]")
  require(byte_order == 0 || byte_order == 1, "byte_order must be either 0 or 1")

  val io = IO(new Bundle {
    val clk_push:   Clock = Input(Clock())
    val clk_pop:    Clock = Input(Clock())
    val rst_n:      Bool  = Input(Bool())
    val push_req_n: Bool  = Input(Bool())
    val flush_n:    Bool  = Input(Bool())
    val pop_req_n:  Bool  = Input(Bool())
    val data_in:    UInt  = Input(UInt(data_in_width.W))
    val push_empty: Bool  = Output(Bool())
    val push_ae:    Bool  = Output(Bool())
    val push_hf:    Bool  = Output(Bool())
    val push_af:    Bool  = Output(Bool())
    val push_full:  Bool  = Output(Bool())
    val ram_full:   Bool  = Output(Bool())
    val part_wd:    Bool  = Output(Bool())
    val push_error: Bool  = Output(Bool())
    val pop_empty:  Bool  = Output(Bool())
    val pop_ae:     Bool  = Output(Bool())
    val pop_hf:     Bool  = Output(Bool())
    val pop_af:     Bool  = Output(Bool())
    val pop_full:   Bool  = Output(Bool())
    val pop_error:  Bool  = Output(Bool())
    val data_out:   UInt  = Output(UInt(data_out_width.W))
  })
}
