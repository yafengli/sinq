package test

import io.sinq.codegen.{StreamProc, TableProc}
import io.sinq.codegen.stream.StreamData
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class CodeGenSuite extends FunSuite with BeforeAndAfter {
  test("init.") {
    val c = classOf[TableProc]
    println(c.getName + "|" + c.getSimpleName + "|" + c.getCanonicalName)
    val pkg = "models"
    TableProc.loop(pkg, "gen")
  }

  test("stream") {
    StreamProc.proc()
  }
}

