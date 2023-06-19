import circt.stage._

object Elaborate extends App {
  private def top       = new add()
  private val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
  (new ChiselStage).execute(args, generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog))
}
