import chisel3._

/**
  * == mux_any ==
  *
  * === Parameters ===
  *
  * | Parameter  | Legal Range  | Default  | Description  |
  * |---------------|--------------|--------------|----------------|
  * | wA            | >=1            | 8               | Width of A input |
  * | wS            | >=1            | 2               | Width of S input |
  * | wZ            | >=1            | 2               | Width of Z output |
  *
  * === Ports ===
  *
  * | Name  | Width  | Direction | Description  |
  * |--------|------------|-----------|------------------------|
  * | A        | wA            | Input      | Data input |
  * | S        | wS            | Input      | Select input |
  * | Z        | wZ            | Output    | Data output |
  *
  * @param wA Width of A input
  * @param wS Width of S input
  * @param wZ Width of Z output
  */
class mux_any(val wA: Int = 8, val wS: Int = 2, val wZ: Int = 2) extends RawModule {
  val io = IO(new Bundle {
    val A: UInt = Input(UInt(wA.W))
    val S: UInt = Input(UInt(wS.W))
    val Z: UInt = Output(UInt(wZ.W))
  })
  protected val U1: CW_mux_any = Module(new CW_mux_any(wA, wS, wZ))
  U1.io.A := io.A
  U1.io.S := io.S
  io.Z    := U1.io.Z
}
