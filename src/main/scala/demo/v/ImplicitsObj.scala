package demo.v


class LineNumber(val num: Int) {
  def +(that: LineNumber): LineNumber = new LineNumber(this.num + that.num)
}

object ImplicitsObj {
  implicit def int2LineNumber(num: Int): LineNumber = new LineNumber(num)

  implicit def lineNumber2Int(ln: LineNumber): Int = ln.num
}
