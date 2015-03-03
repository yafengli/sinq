package demo.v


case class A(val name: String, val id: Long)

object IA {
  implicit def stream(a: A) = new SayHi[A] {}

  def apply(name: String): A = {
    A(name, -1l)
  }
}