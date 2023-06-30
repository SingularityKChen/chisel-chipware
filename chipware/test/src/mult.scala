// filename: mult.scala
import chisel3._

class mult(val wA: Int = 8, val wB: Int = 8) extends RawModule {
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wB.W))
    val TC: Bool = Input(Bool())
    val Z:  UInt = Output(UInt((wA + wB - 1).W))
  })

  protected val U1: CW_mult = Module(new CW_mult(wA, wB))
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.TC := io.TC
  io.Z     := U1.io.Z
}
