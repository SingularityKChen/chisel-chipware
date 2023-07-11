import chisel3._
import chisel3.experimental._
import chisel3.util.log2Ceil

/**
  *  == CW_asymfifoctl_s2_sf ==
  *
  *  === Abstract ===
  *
  *  Asym. Synch. (Dual-Clock) FIFO Controller - Static Flags
  *
  *  === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | data_in_width  | 1 to 256 | 3 | Width of the data_in bus. data_in_width must be in an integer-multiple relationship with data_out_width. That is, either data_in_width = K x data_out_width, or data_out_width = K x data_in_width, where K is an integer. |
  * | data_out_width  | 1 to 256 | 12 | Width of the data_out bus. data_out_width must be in an integer-multiple relationship with data_in_width. That is, either data_in_width = K x data_out_width, or data_out_width = K x data_in_width, where K is an integer. |
  * | depth  | 4 to 224 | 8 | Number of words that can be stored in FIFO |
  * | push_ae_lvl  | 1 to depth - 1 | 2 | Almost empty level for the push_ae output port (the number of words in the FIFO at or below which the push_ae flag is active) |
  * | push_af_lvl  | 1 to depth - 1 | 2 | Almost full level for the push_af output port (the number of empty memory locations in the FIFO at which the push_af flag is active) |
  * | pop_ae_lvl  | 1 to depth - 1 | 2 | Almost empty level for the pop_ae output port (the number of words in the FIFO at or below which the pop_ae flag is active) |
  * | pop_af_lvl  | 1 to depth - 1 | 2 | Almost full level for the pop_af output port (the number of empty memory locations in the FIFO at which the pop_af flag is active) |
  * | err_mode  | 0 or 1 | 0 | Error mode 0 = stays active until reset [latched], 1 = active only as long as error condition exists [unlatched]) |
  * | push_sync  | 0 to 4 | 2 | Push flag synchronization mode Default: 2 0 = single clock design no synchronization mechanism is implemented 1 = 2-sample synchronizer 1st sample neg-edge 2nd sample pos-edge 2 = 2-sample synchronizer both sample pos-edge 3 = 3-stage synchronizer all samples pos-edge 4 = 4-stage synchronizer all samples pos-edge |
  * | pop_sync  | 0 to 4 | 2 | Pop flag synchronization mode Default: 2 0 = single clock design no synchronization mechanism is implemented 1 = 2-sample synchronizer 1st sample neg-edge 2nd sample pos-edge 2 = 2-sample synchronizer both sample pos-edge 3 = 3-stage synchronizer all samples pos-edge 4 = 4-stage synchronizer all samples pos-edge |
  * | rst_mode  | 0 or 1 | 0 | Reset mode 0 = asynchronous reset, 1 = synchronous reset) |
  * | byte_order  | 0 or 1 | 0 | Order of bytes or subword within a word Default: 0 0 = first byte is in most significant bits position; 1 = first byte is in the least significant bits position |
  *
  *  === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | clk_push | 1 bit | Input | Input clock for push interface |
  * | clk_pop | 1 bit | Input | Input clock for pop interface |
  * | rst_n | 1 bit | Input | Reset input, active low |
  * | push_req_n | 1 bit | Input | FIFO push request, active low |
  * | flush_n | 1 bit | Input | Flushes the partial word into memory (fills in 0's) (for data_in_width < data_out_width only) |
  * | pop_req_n | 1 bit | Input | FIFO pop request, active low |
  * | data_in | data_in_width bit(s) | Input | FIFO data to push |
  * | rd_data | max(data_in_width, data_out_width) bit(s) | Input | RAM data input to FIFO controller |
  * | we_n | 1 bit | Output | Write enable output for write port of RAM, active low |
  * | push_empty | 1 bit | Output | FIFO empty a output flag synchronous to clk_push, active high |
  * | push_ae | 1 bit | Output | FIFO almost emptya output flag synchronous to clk_push, active high (determined by push_ae_lvl parameter) |
  * | push_hf | 1 bit | Output | FIFO half fulla output flag synchronous to clk_push, active high |
  * | push_af | 1 bit | Output | FIFO almost fulla output flag synchronous to clk_push, active high (determined by push_af_lvl parameter) |
  * | push_full | 1 bit | Output | FIFO's RAM fulla output flag (including the input buffer of FIFO controller for data_in_width < data_out_width) synchronous to clk_push, active high |
  * | ram_full | 1 bit | Output | FIFO's RAM (excluding the input buffer of FIFO controller for data_in_width < data_out_width) full output flag synchronous to clk_push, active high |
  * | part_wd | 1 bit | Output | Partial word accumulated in the input buffer synchronous to clk_push, active high (for data_in_width < data_out_width only; otherwise, tied low) |
  * | push_error | 1 bit | Output | FIFO push error (overrun) output flag synchronous to clk_push, active high |
  * | pop_empty | 1 bit | Output | FIFO empty b output flag synchronous to clk_pop, active high |
  * | pop_ae | 1 bit | Output | FIFO almost emptyb output flag synchronous to clk_pop, active high (determined by pop_ae_lvl parameter) |
  * | pop_hf | 1 bit | Output | FIFO half fullb output flag synchronous to clk_pop, active high |
  * | pop_af | 1 bit | Output | FIFO almost fullb output flag synchronous to clk_pop, active high (determined by pop_af_lvl parameter) |
  * | pop_full | 1 bit | Output | FIFO's RAM fullb output flag (excluding the input buffer of FIFO controller for case data_in_width < data_out_width) synchronous to clk_pop, active high |
  * | pop_error | 1 bit | Output | FIFO pop error (underrun) output flag synchronous to clk_pop, active high |
  * | wr_data | max(data_in_width, data_out_width) bit(s) | Output | FIFO controller output data to RAM |
  * | wr_addr | ceil(log2[depth]) bit(s) | Output | Address output to write port of RAM |
  * | rd_addr | ceil(log2[depth]) bit(s) | Output | Address output to read port of RAM |
  * | data_out | data_out_width bit(s) | Output | FIFO data to pop |
  *
  *  @param data_in_width Width of the data_in bus. data_in_width must be in an integer-multiple relationship with data_out_width. That is, either data_in_width = K x data_out_width, or data_out_width = K x data_in_width, where K is an integer.
  *  @param data_out_width Width of the data_out bus. data_out_width must be in an integer-multiple relationship with data_in_width. That is, either data_in_width = K x data_out_width, or data_out_width = K x data_in_width, where K is an integer.
  *  @param depth Number of words that can be stored in FIFO
  *  @param push_ae_lvl Almost empty level for the push_ae output port (the number of words in the FIFO at or below which the push_ae flag is active)
  *  @param push_af_lvl Almost full level for the push_af output port (the number of empty memory locations in the FIFO at which the push_af flag is active)
  *  @param pop_ae_lvl Almost empty level for the pop_ae output port (the number of words in the FIFO at or below which the pop_ae flag is active)
  *  @param pop_af_lvl Almost full level for the pop_af output port (the number of empty memory locations in the FIFO at which the pop_af flag is active)
  *  @param err_mode Error mode 0 = stays active until reset [latched], 1 = active only as long as error condition exists [unlatched])
  *  @param push_sync Push flag synchronization mode Default: 2 0 = single clock design no synchronization mechanism is implemented 1 = 2-sample synchronizer 1st sample neg-edge 2nd sample pos-edge 2 = 2-sample synchronizer both sample pos-edge 3 = 3-stage synchronizer all samples pos-edge 4 = 4-stage synchronizer all samples pos-edge
  *  @param pop_sync Pop flag synchronization mode Default: 2 0 = single clock design no synchronization mechanism is implemented 1 = 2-sample synchronizer 1st sample neg-edge 2nd sample pos-edge 2 = 2-sample synchronizer both sample pos-edge 3 = 3-stage synchronizer all samples pos-edge 4 = 4-stage synchronizer all samples pos-edge
  *  @param rst_mode Reset mode 0 = asynchronous reset, 1 = synchronous reset)
  *  @param byte_order Order of bytes or subword within a word Default: 0 0 = first byte is in most significant bits position; 1 = first byte is in the least significant bits position
  */
class CW_asymfifoctl_s2_sf(
  val data_in_width:  Int = 3,
  val data_out_width: Int = 12,
  val depth:          Int = 8,
  val push_ae_lvl:    Int = 2,
  val push_af_lvl:    Int = 2,
  val pop_ae_lvl:     Int = 2,
  val pop_af_lvl:     Int = 2,
  val err_mode:       Int = 0,
  val push_sync:      Int = 2,
  val pop_sync:       Int = 2,
  val rst_mode:       Int = 0,
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
    ) {
  require(
    data_in_width >= 1 && data_in_width <= 256,
    s"data_in_width must be in range [1, 256], but got $data_in_width"
  )
  require(
    data_out_width >= 1 && data_out_width <= 256,
    s"data_out_width must be in range [1, 256], but got $data_out_width"
  )
  require(depth >= 4 && depth <= 16777216, s"depth must be in range [4, 16777216], but got $depth")
  require(
    push_ae_lvl >= 1 && push_ae_lvl <= depth - 1,
    s"push_ae_lvl must be in range [1, depth-1], but got $push_ae_lvl"
  )
  require(
    push_af_lvl >= 1 && push_af_lvl <= depth - 1,
    s"push_af_lvl must be in range [1, depth-1], but got $push_af_lvl"
  )
  require(pop_ae_lvl >= 1 && pop_ae_lvl <= depth - 1, s"pop_ae_lvl must be in range [1, depth-1], but got $pop_ae_lvl")
  require(pop_af_lvl >= 1 && pop_af_lvl <= depth - 1, s"pop_af_lvl must be in range [1, depth-1], but got $pop_af_lvl")
  require(err_mode >= 0 && err_mode <= 1, s"err_mode must be either 0 or 1, but got $err_mode")
  require(push_sync >= 0 && push_sync <= 4, s"push_sync must be in range [0, 4], but got $push_sync")
  require(pop_sync >= 0 && pop_sync <= 4, s"pop_sync must be in range [0, 4], but got $pop_sync")
  require(rst_mode >= 0 && rst_mode <= 1, s"rst_mode must be either 0 or 1, but got $rst_mode")
  require(byte_order >= 0 && byte_order <= 1, s"byte_order must be either 0 or 1, but got $byte_order")

  protected val max_data_width: Int = math.max(data_in_width, data_out_width)

  val io = IO(new Bundle {
    val clk_push:   Bool = Input(Bool())
    val clk_pop:    Bool = Input(Bool())
    val rst_n:      Bool = Input(Bool())
    val push_req_n: Bool = Input(Bool())
    val flush_n:    Bool = Input(Bool())
    val pop_req_n:  Bool = Input(Bool())
    val data_in:    UInt = Input(UInt(data_in_width.W))
    val rd_data:    UInt = Input(UInt(max_data_width.W))
    val we_n:       Bool = Output(Bool())
    val push_empty: Bool = Output(Bool())
    val push_ae:    Bool = Output(Bool())
    val push_hf:    Bool = Output(Bool())
    val push_af:    Bool = Output(Bool())
    val push_full:  Bool = Output(Bool())
    val ram_full:   Bool = Output(Bool())
    val part_wd:    Bool = Output(Bool())
    val push_error: Bool = Output(Bool())
    val pop_empty:  Bool = Output(Bool())
    val pop_ae:     Bool = Output(Bool())
    val pop_hf:     Bool = Output(Bool())
    val pop_af:     Bool = Output(Bool())
    val pop_full:   Bool = Output(Bool())
    val pop_error:  Bool = Output(Bool())
    val wr_data:    UInt = Output(UInt(max_data_width.W))
    val wr_addr:    UInt = Output(UInt(log2Ceil(depth).W))
    val rd_addr:    UInt = Output(UInt(log2Ceil(depth).W))
    val data_out:   UInt = Output(UInt(data_out_width.W))
  })
}
