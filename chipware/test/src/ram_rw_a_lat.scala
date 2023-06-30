import chisel3._
import chisel3.util.log2Ceil
import circt.stage._
import utest._

class ram_rw_a_lat(val data_width: Int = 16, val depth: Int = 8, val rst_mode: Int = 0) extends RawModule {
  // Define ports with type annotations
  val io = IO(new Bundle {
    val rst_n:    Bool = Input(Bool()) // Active low reset
    val cs_n:     Bool = Input(Bool()) // Chip select
    val wr_n:     Bool = Input(Bool()) // Write enable
    val rw_addr:  UInt = Input(UInt((log2Ceil(depth).W))) // Address for read/write
    val data_in:  UInt = Input(UInt(data_width.W)) // Input data
    val data_out: UInt = Output(UInt(data_width.W)) // Output data
  })
  // Instantiate the BlackBox module
  protected val U0: CW_ram_rw_a_lat = Module(new CW_ram_rw_a_lat(data_width, depth, rst_mode))

  // Connect inputs and outputs
  U0.io.rst_n   := io.rst_n
  U0.io.cs_n    := io.cs_n
  U0.io.wr_n    := io.wr_n
  U0.io.rw_addr := io.rw_addr
  U0.io.data_in := io.data_in

  // Assign outputs
  io.data_out := U0.io.data_out
}

object ram_rw_a_lat extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate ram_rw_a_lat") {
      def top = new ram_rw_a_lat()

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
