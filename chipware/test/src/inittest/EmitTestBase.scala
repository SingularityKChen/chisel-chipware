import utest._

abstract class EmitTestBase extends TestSuite {
  val targetDir: String = "./build"
  val chiselArgs: Array[String] = Array(
    f"--target-dir=$targetDir"
  )
  val firtoolArgs: Array[String] = Array(
    "-disable-all-randomization"
  )
  val tests: Tests
}
