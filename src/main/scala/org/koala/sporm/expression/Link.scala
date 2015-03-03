package org.koala.sporm.expression

trait Link {
  val buffer = new StringBuffer()

  def and(link: Link): Unit = {
    this.buffer.append(link.buffer.toString)
  }

  def or(link: Link): Unit
}

object Link {


}
