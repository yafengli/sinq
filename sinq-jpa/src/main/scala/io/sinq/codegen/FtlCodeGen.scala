package io.sinq.codegen

import java.io.{BufferedWriter, File, FileWriter, StringWriter}

import freemarker.template.Configuration

import scala.collection.mutable

object FtlCodeGen extends App {

  val cfg = new Configuration(Configuration.VERSION_2_3_22)
  cfg.setClassForTemplateLoading(this.getClass, "/META-INF/ftl")
  cfg.setDefaultEncoding("UTF-8")

  val select = cfg.getTemplate("select.ftl")
  val writer = new StringWriter()
  val map = mutable.HashMap[String, Any]()
  map += ("package" -> "com.greatbit.ok")
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
}


case class FtlData(val pkg: String, val selects: Seq[])

case class S(val tpe: String, val cs: Seq[C])

case class C(var name: String, var tpe: String)
