package test

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
 * User: YaFengLi
 * Date: 12-12-11
 * Time: 上午11:08
 */
@RunWith(classOf[JUnitRunner])
class StringSuite extends FunSuite with BeforeAndAfter {
  before {
  }

  after {
  }

  test("Map") {
    val l = List(1, 2, 3, 4, 5)
    val l1 = l.map(_ + 2)
    println(l)
    println("###############")
    println(l1)
    println("###############")
    val l2 = l.flatMap(t => List(t - 1, t, t + 1))
    println(l)
    println("###############")
    println(l2)


    val m = Map(1 -> 1, 2 -> 2, 3 -> 3, 4 -> 4)

    val m1 = m.mapValues(_ * 2)
    println(m)
    println("###############")
    println(m1)
    println("###############")
    val m3 = m.flatMap(e => List(e._2))
    println(m3)
    println("###############")
    val m4 = m.flatMap(e => List(e))
    println(m4)
  }
}



