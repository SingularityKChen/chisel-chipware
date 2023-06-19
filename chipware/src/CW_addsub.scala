import chisel3._
import chisel3.experimental._ // To enable experimental features

class CW_addsub(val wA: Int = 8) extends BlackBox(
  Map(
    "wA" -> wA
  )
) {
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val B: UInt = Input(UInt(wA.W))
    val CI: Bool = Input(Bool())
    val SUB: Bool = Input(Bool())
    val Z: UInt = Output(UInt(wA.W))
    val CO: Bool = Output(Bool())
  })
}
