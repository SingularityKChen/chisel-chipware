import chisel3._
import chiseltest._
import chiseltest.simulator.VerilatorFlags
import firrtl2.annotations.NoTargetAnnotation
import utest._

class addWithBlackBox(val wA: Int = 4) extends Module {
  require(wA >= 1, "wA must be >= 1")
  val io = IO(new Bundle {
    val A:  UInt = Input(UInt(wA.W))
    val B:  UInt = Input(UInt(wA.W))
    val CI: UInt = Input(UInt(1.W))
    val Z:  UInt = Output(UInt(wA.W))
    val CO: UInt = Output(UInt(1.W))
  })
  protected val U1: CW_add = Module(new CW_add(wA))
  U1.io.A  := io.A
  U1.io.B  := io.B
  U1.io.CI := io.CI
  io.Z     := U1.io.Z
  io.CO    := U1.io.CO

  def addPath(blackBoxFileSeq: Seq[String]): Unit = {
    blackBoxFileSeq.foreach(blackBoxPath => U1.addPath(blackBoxPath))
  }
}

object addWithBlackBox {
  def simWithBBoxVerilog(blackBoxFileSeq: Seq[String]): addWithBlackBox = {
    val addWithBlackBox = new addWithBlackBox
    addWithBlackBox.addPath(blackBoxFileSeq)
    addWithBlackBox
  }
}

object DemoTest extends TestSuite with ChiselUtestTester {
  val bboxFileSeq: Seq[String] = Seq(
    "chipware/test/resources/CW_add_demo.v"
  )
  val annotationSeq: Seq[NoTargetAnnotation] = Seq(
    WriteVcdAnnotation,
    VerilatorBackendAnnotation,
    // Disable all lint warnings
    VerilatorFlags(Seq("-Wno-lint"))
  )
  val tests: Tests = Tests {
    test("add should work with blackbox") {
      testCircuit(addWithBlackBox.simWithBBoxVerilog(bboxFileSeq), annotationSeq = annotationSeq) { dut =>
        dut.io.A.poke(1.U)
        dut.io.B.poke(2.U)
        dut.io.CI.poke(0.U)
        dut.io.Z.expect(3.U)
        dut.io.CO.expect(0.U)
      }
    }
  }
}
