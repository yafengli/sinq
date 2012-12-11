package org.sporm.jpa


/**
 * User: YaFengLi
 * Date: 12-12-6
 * Time: 上午11:30
 */
abstract class SPQL[T: Manifest] {

  import SPQL._

  private val buffer = new StringBuffer

  def getType = implicitly[Manifest[T]].runtimeClass

  def select(): SPQL[T] = {
    if (buffer.toString.trim.length == 0) {
      buffer.append("select * from ").append(getType.getCanonicalName).append(STR_BLANK)
      this
    }
    else
      throw new Exception("ERROR OPERATION")
  }

  def where(qs: String): SPQL[T] = {
    if (buffer.toString.indexOf(" from ") > 0) {
      buffer.append(qs).append(STR_BLANK)
      this
    }
    else
      throw new Exception("ERROR OPERATION")
  }

  def limit(limit: Int): SPQL[T] = {
    if (buffer.toString.indexOf(" from ") > 0) {
      buffer.append(" limit ").append(limit.toString).append(STR_BLANK)
      this
    }
    else
      throw new Exception("ERROR OPERATION")
  }

  def offset(offset: Int): SPQL[T] = {
    if (buffer.toString.indexOf(" from ") > 0) {
      buffer.append(" offset ").append(offset.toString).append(STR_BLANK)
      this
    }
    else
      throw new Exception("ERROR OPERATION")

  }

  def order(orders: Array[String]): SPQL[T] = {
    if (buffer.toString.indexOf(" from ") > 0) {
      buffer.append(" order by ").append(orders.mkString(",")).append(STR_BLANK)
      this
    }
    else
      throw new Exception("ERROR OPERATION")
  }

  def sort(sort: String): SPQL[T] = {
    if (buffer.toString.indexOf(" from ") > 0) {
      buffer.append(sort).append(STR_BLANK)
      this
    }
    else
      throw new Exception("ERROR OPERATION")
  }

  def sql(): String = {
    buffer.toString
  }
}

object SPQL {
  val STR_BLANK = " "
}
