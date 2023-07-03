// filename: fifoctl_cache.scala
import chisel3._
import circt.stage._
import utest._

class fifoctl_cache(val width: Int = 8, val cache_depth: Int = 3) extends RawModule {
  protected val word_count_width: Int = if (cache_depth > 1) 2 else 1
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

  protected val U1: CW_fifoctl_cache = Module(new CW_fifoctl_cache(width, cache_depth))
  U1.io.rst_d_n               := io.rst_d_n
  U1.io.init_d_n              := io.init_d_n
  U1.io.clk_d                 := io.clk_d
  U1.io.user_pop_d_n          := io.user_pop_d_n
  io.data_d                   := U1.io.data_d
  io.cache_pop_d_n            := U1.io.cache_pop_d_n
  U1.io.rd_data_d             := io.rd_data_d
  U1.io.flagRNE               := io.flagRNE
  U1.io.flagRNEP              := io.flagRNEP
  U1.io.cache_word_count_next := io.cache_word_count_next
  io.cache_encoded_word_count := U1.io.cache_encoded_word_count
}

object fifoctl_cache extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate fifoctl_cache") {
      def top = new fifoctl_cache()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
