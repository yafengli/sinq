package demo.v

abstract class SayHi[T: Manifest] {
  def sayHi(msg: String): Unit = println(s"#${Thread.currentThread().getId}#Hi ${msg}")
}

object DemoSinqStream {

  implicit def stream[T: Manifest](t: T) = new SayHi[T] {}
}