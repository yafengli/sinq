package io.sinq.codegen

import java.io.{BufferedWriter, File, FileWriter}


object TemplateStringSuite extends App {

  withWriter(new File("f:/tmp/select.code.txt")) {
    w =>
      (2 to 22).foreach(i => w.write(select(i)))
  }

  withWriter(new File("f:/tmp/match.code.txt")) {
    w =>
      w.write(pattern(22))
  }

  private def select(count: Int): String = {
    val buffer = new StringBuffer()

    buffer.append("def select[")
    //1
    (1 to (count - 1)).foreach(i => buffer.append(s"T${i},"))
    buffer.append(s"T${count}](")
    //2
    (1 to (count - 1)).foreach(i => buffer.append(s"c${i}:Column[T${i}],"))
    buffer.append(s"c${count}:Column[T${count}]):From[(")
    //3
    (1 to (count - 1)).foreach(i => buffer.append(s"T${i},"))
    buffer.append(s"T${count})] = { ").append("\n")
    buffer.append("val info = QueryInfo(this)").append("\n")

    //4
    (1 to (count - 1)).foreach(i => buffer.append(s"info.selectFields += c${i}").append("\n"))
    buffer.append(s"info.selectFields += c${count}").append("\n")
    buffer.append("FromImpl[(")
    //5
    (1 to (count - 1)).foreach(i => buffer.append(s"T${i},"))
    buffer.append(s"T${count}").append(")](info)").append("\n").append("}\n")

    buffer.toString
  }

  private def pattern(count: Int): String = {
    val buffer = new StringBuffer()
    (1 to count).foreach {
      i =>
        buffer.append("case Array(")
        (1 to (i - 1)).foreach {
          j =>
            buffer.append(s"_${j},")
        }
        buffer.append(s"_${i})").append(" => (")

        (1 to (i - 1)).foreach {
          j =>
            buffer.append(s"_${j},")
        }
        buffer.append(s"_${i})").append("\n")
    }
    buffer.toString
  }

  def withWriter(f: File)(call: (BufferedWriter) => Unit): Unit = {
    val writer = new BufferedWriter(new FileWriter(f))
    try {
      call(writer)
      writer.flush()
    }
    finally {
      writer.close()
    }
  }
}

