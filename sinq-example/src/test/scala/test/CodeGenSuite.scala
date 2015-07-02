package test

import io.sinq.codegen.TableProc
import io.sinq.codegen.stream.TypeDataMap
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class CodeGenSuite extends FunSuite with BeforeAndAfter {

  test("CodeGen.") {
    println("##Scan [models.*]##")
    new TableProc("models", "gen", TypeDataMap.factory()).proc()
  }
}

