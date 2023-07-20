import chisel3._
import chisel3.experimental._
import chisel3.util.HasBlackBoxPath

/**
  * == CW_satrnd ==
  *
  * === Abstract ===
  *
  * Arithmetic Saturation and Rounding Logic
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | W  | >=2  | 16  | Bit width of din |
  * | M  | >L and <=W-1  | 15  | Saturation position |
  * | L  | >=0 and < M  | 0  | Rounding position |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | din  | W  | Input | Signed or unsigned integer number |
  * | tc  | 1  | Input | 0: din and dout are interpreted as unsigned integers 1: din and dout are interpreted as signed integers |
  * | sat  | 1  | Input | 0: no saturation 1: saturation |
  * | rnd  | 1  | Input | 0: Round to -infinity 1: Round to nearest, up |
  * | dout  | M-L+1  | Output | Signed/unsigned integer number |
  * | ov  | 1  | Output | Overflow Status Flag |
  *
  * @param W Bit width of din
  * @param M Saturation position
  * @param L Rounding position
  */
class CW_satrnd(val W: Int = 16, val M: Int = 15, val L: Int = 0)
    extends BlackBox(
      Map(
        "W" -> W,
        "M" -> M,
        "L" -> L
      )
    )
    with HasBlackBoxPath {
  require(W >= 2, s"W must be >= 2, but got $W")
  require(M > L && M <= W - 1, s"M must be > L and <= W - 1, but got M = $M, L = $L, W = $W")
  require(L >= 0 && L < M, s"L must be >= 0 and < M, but got L = $L, M = $M")

  val io = IO(new Bundle {
    val din:  UInt = Input(UInt(W.W))
    val tc:   Bool = Input(Bool())
    val sat:  Bool = Input(Bool())
    val rnd:  Bool = Input(Bool())
    val dout: UInt = Output(UInt((M - L + 1).W))
    val ov:   Bool = Output(Bool())
  })
}
