import chisel3._
import circt.stage._
import chisel3.util._
import utest._

class satrnd(val W: Int = 16, val M: Int = 15, val L: Int = 0) extends RawModule {
  protected val bit_width_W: Int = log2Ceil(W)
  protected val bit_width_M: Int = log2Ceil(M)
  val io = IO(new Bundle {
    val din:  UInt = Input(UInt(W.W))
    val tc:   Bool = Input(Bool())
    val sat:  Bool = Input(Bool())
    val rnd:  Bool = Input(Bool())
    val dout: UInt = Output(UInt((M - L + 1).W))
    val ov:   Bool = Output(Bool())
  })

  require(W >= 2, s"W must be >= 2, but got $W")
  require(M > L && M <= W - 1, s"M must be > L and <= W - 1, but got M = $M, L = $L, W = $W")
  require(L >= 0 && L < M, s"L must be >= 0 and < M, but got L = $L, M = $M")

  protected val U1: CW_satrnd = Module(new CW_satrnd(W, M, L))
  U1.io.din := io.din
  U1.io.tc  := io.tc
  U1.io.sat := io.sat
  U1.io.rnd := io.rnd
  io.dout   := U1.io.dout
  io.ov     := U1.io.ov
}

object satrnd extends TestSuite {
  val tests: Tests = Tests {
    test("should instantiate satrnd") {
      def top = new satrnd(16, 15, 0)

      val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
      (new ChiselStage).execute(
        args        = Array("--target-dir", "./build"),
        annotations = generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog)
      )
    }
  }
}
