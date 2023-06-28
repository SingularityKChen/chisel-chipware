// filename: ram_rw_s_lat.scala
import circt.stage._
import chisel3._
import chisel3.util._
import utest._

class ram_rw_s_lat(val data_width: Int = 16, val depth: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val clk:      Clock = Input(Clock()) // Clock signal
    val cs_n:     Bool  = Input(Bool()) // Chip select signal (active low)
    val wr_n:     Bool  = Input(Bool()) // Write enable signal (active low)
    val rw_addr:  UInt  = Input(UInt(log2Ceil(depth).W)) // Read/write address
    val data_in:  UInt  = Input(UInt(data_width.W)) // Input data
    val data_out: UInt  = Output(UInt(data_width.W)) // Output data
  })
  // Declare parameter bit_width_depth
  protected val bit_width_depth: Int = log2Ceil(depth)

  // Instantiate the Chisel BlackBox
  val cwRam: CW_ram_rw_s_lat = Module(new CW_ram_rw_s_lat(data_width, depth))

  // Connect ports between modules
  cwRam.io.clk     := io.clk
  cwRam.io.cs_n    := io.cs_n
  cwRam.io.wr_n    := io.wr_n
  cwRam.io.rw_addr := io.rw_addr
  cwRam.io.data_in := io.data_in
  io.data_out      := cwRam.io.data_out
}

object ram_rw_s_lat extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ram_rw_s_lat") {
      def top = new ram_rw_s_lat()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
