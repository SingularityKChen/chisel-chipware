import chisel3._
import chisel3.experimental._ // To enable experimental features

class CW_addsub_dx(width: Int = 4, p1_width: Int = 2) extends BlackBox(
  Map(
    "width" -> width,
    "p1_width" -> p1_width
  )
) {
  val io = IO(new Bundle {
    val a: UInt = Input(UInt(width.W))
    val b: UInt = Input(UInt(width.W))
    val ci1: Bool = Input(Bool())
    val ci2: Bool = Input(Bool())
    val addsub: Bool = Input(Bool())
    val tc: Bool = Input(Bool())
    val sat: Bool = Input(Bool())
    val avg: Bool = Input(Bool())
    val dplx: Bool = Input(Bool())
    val sum: UInt = Output(UInt(width.W))
    val co1: Bool = Output(Bool())
    val co2: Bool = Output(Bool())
  })
}
