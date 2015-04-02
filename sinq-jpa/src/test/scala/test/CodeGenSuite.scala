package test

import io.sinq.codegen.TableProc
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class CodeGenSuite extends FunSuite with BeforeAndAfter {
  test("init.") {
    val pkg = "models"
    TableProc.loop(pkg)
  }
}

