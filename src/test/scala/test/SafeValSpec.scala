package test

import org.specs2.mutable
import scala.concurrent.forkjoin.RecursiveAction

/**
 * User: ya_feng_li@163.com
 * Date: 13-7-1
 * Time: 上午10:59
 */
class SafeValSpec extends mutable.Specification {
  "SafeValSpec" should {
    "SafeValSpec" in {
      import java.util.concurrent.TimeUnit
      import SafeValSpec._
      val task = new SafeValRun(2)
      pool.submit(task)
      pool.shutdown()
      pool.awaitTermination(20, TimeUnit.SECONDS)
    }
  }
}

object SafeValSpec {

  import scala.concurrent.forkjoin.ForkJoinPool

  val pool = new ForkJoinPool(8)
}

trait T {
  val id = Thread.currentThread().getId.toString
}

class Ti extends T {

}

case class SafeValRun(var count: Int) extends RecursiveAction {

  import DB._

  def compute() {
    if (count > 1) {
      import scala.concurrent.forkjoin.ForkJoinTask
      ForkJoinTask.invokeAll(new SafeValRun(count - 1), new SafeValRun(1))
    }
    else {
      val id = Thread.currentThread().getId
      val ti = new Ti
      println(f"#id:${id} size:${ti.id}")
      size += 1
    }
  }
}
