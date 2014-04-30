package test

import concurrent.forkjoin.{ForkJoinTask, RecursiveAction}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * User: YaFengLi
 * Date: 13-1-18
 * Time: 上午11:20
 */
@RunWith(classOf[JUnitRunner])
class ForkJoinSuite extends FunSuite with BeforeAndAfter {
//  "Hello World!" should {
//    "Fork" in {
//
//      val listBuffer = ListBuffer[String]()
//      for (i <- 0 until 1000) {
//        listBuffer += i.toString
//      }
//      val start = System.currentTimeMillis()
//      val task = new DemoAction(listBuffer.toList, 4)
//      val pool = new ForkJoinPool()
//      pool.submit(task)
//      pool.shutdown()
//      pool.awaitTermination(30, TimeUnit.SECONDS)
//      val end = System.currentTimeMillis()
//      println(f"#time use ${end - start}ms.")
//    }
//  }

}


case class DemoAction(var list: List[String], var count: Int) extends RecursiveAction {
  def compute() {
    if (list.size > count) {
      ForkJoinTask.invokeAll(new DemoAction(list.take(count), count), new DemoAction(list.takeRight(list.size - count), count))
    }
  }
}