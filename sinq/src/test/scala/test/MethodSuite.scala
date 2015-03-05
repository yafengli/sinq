package test

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

@RunWith(classOf[JUnitRunner])
class MethodSuite extends FunSuite with BeforeAndAfter {
  before {

  }
  after {

  }

  test("hello runnable") {
    procTest("hello", () => {
      println(s"id:${Thread.currentThread().getId}")
    })
    
    procTest("run",() =>{})
  }

  def procTest(name: String, call: () => Unit): Unit = {
    println(s"Hello ${name}.")
    Future {
      call
    }
  }

  def procTest(name: String, run: Runnable): Unit = {
    println(s"Hello ${name}.")
    Future {
      run.run()
    }
  }
}

