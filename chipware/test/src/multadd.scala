import chisel3._

class multadd(val wA: Int = 8, val wB: Int = 8, val wC: Int = 16, val wZ: Int = 16) extends RawModule {
  require(wA >= 1, "wA must be >= 1")
  require(wB >= 1, "wB must be >= 1")
  require(wC >= 1, "wC must be >= 1")
  require(wZ >= 1, "wZ must be >= 1")
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wB.W))
    val C:  UInt = Input(UInt(wC.W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt(wZ.W))
  })
  protected val U1: CW_multadd = Module(new CW_multadd(wA, wB, wC, wZ))
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.C  := io.C
  U1.io.TC := io.TC
  io.Z     := U1.io.Z
}
