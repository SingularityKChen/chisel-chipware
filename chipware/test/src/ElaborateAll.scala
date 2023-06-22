import circt.stage._

object ElaborateAll extends App {
  private def elaborate[T <: chisel3.RawModule](gen: => T): Unit = {
    val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => gen))
    (new ChiselStage).execute(args, generator :+ CIRCTTargetAnnotation(CIRCTTarget.SystemVerilog))
  }

  private val chipWare = Seq(
    new add(),
    new arbiter_sp(4, 1, 0, 0),
    new arbiter_2t(4, 2, 1, 0, 1),
    new arbiter_dp(4, 1, 0, 0),
    new arbiter_fcfs(),
    new arbiter_rr(),
    new fp_addsub(),
    new fp_exp(),
    new fp_mac(),
    new fp_sub(),
    new ashiftr(),
    new fp_cmp(),
    new fp_flt2i(),
    new fp_mult(),
    new fp_div(),
    new fp_i2flt(),
    new fp_recip(),
    new fp_div_seq(),
    new fp_ln(),
    new fp_sincos(),
    new fp_add(),
    new fp_exp2(),
    new fp_log2(),
    new fp_sqrt(),
  )

  chipWare.foreach(gen => elaborate(gen))
}
