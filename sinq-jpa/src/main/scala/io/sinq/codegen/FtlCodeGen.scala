package io.sinq.codegen

import java.io.{BufferedWriter, File, FileWriter, StringWriter}

import freemarker.template.Configuration

import scala.beans.BeanProperty
import scala.collection.mutable
import java.util

object FtlCodeGen extends App {

  val cfg = new Configuration(Configuration.VERSION_2_3_22)
  cfg.setClassForTemplateLoading(this.getClass, "/META-INF/ftl")
  cfg.setDefaultEncoding("UTF-8")

  val select = cfg.getTemplate("select.ftl")
  val writer = new StringWriter()
  val map = store(22)
  println("map:" + map)
  select.process(map, writer)
  println("###############")
  println(writer.toString)
  println("###############")

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

  def store(count: Int): util.HashMap[String, Any] = {
    val map = new util.HashMap[String, Any]()
    val dm = new util.HashMap[String, Any]()
    val list = new util.ArrayList[Any]()
    dm.put("pkg", "io.sinq")

    (1 to count).foreach {
      i =>
        val cmap = new util.HashMap[String, Any]()
        val tpes = for (j <- 1 to i) yield s"T${j}"

        cmap.put("tpe", tpes.mkString(","))

        val clist = new util.ArrayList[Any]()
        (1 to tpes.size).foreach {
          k =>
            val ccmap = new util.HashMap[String, Any]()
            ccmap.put("name", s"c${k}")
            ccmap.put("tpe", s"Column[T${k}]")
            clist.add(ccmap)
        }
        cmap.put("cs", clist)
        list.add(cmap)
    }
    dm.put("teps", list)
    map.put("data", dm)
    map
  }
}


class FtlData {
  @BeanProperty var pkg: String = _
  @BeanProperty var teps = mutable.ArrayBuffer[S]()
}

class S {
  @BeanProperty var tpe: String = _
  @BeanProperty val cs = mutable.ArrayBuffer[C]()
}

class C {
  @BeanProperty var name: String = _
  @BeanProperty var tpe: String = _
}
