package test

import io.sinq.codegen.stream.TypeDataMap
import io.sinq.codegen.{StreamProc, TableProc}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

@RunWith(classOf[JUnitRunner])
class CodeGenSuite extends FunSuite with BeforeAndAfter {
  test("models.postgres.init.") {
    val c = classOf[TableProc]
    println(c.getName + "|" + c.getSimpleName + "|" + c.getCanonicalName)

    val tableProc = new TableProc("models", "gen", TypeDataMap.HIBERNATE)
    tableProc.proc()
  }

  test("stream") {
    StreamProc.proc()
  }
}

