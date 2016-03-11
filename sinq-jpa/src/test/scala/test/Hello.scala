package test

class Hello(val name: String = "123")

object Hello {
  def apply(name: String) = {
    println(">>>>>>>>>>>>>>>>>>>>" + name)
    new Hello(name)
  }

  def test(): Unit = {
    val hello = Hello("test")
    println(s"hello:${hello.name}")
  }
}

