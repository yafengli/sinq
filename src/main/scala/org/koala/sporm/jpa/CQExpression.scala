package org.koala.sporm.jpa

import javax.persistence.criteria._

case class CQExpression[T, X](builder: CriteriaBuilder, query: CriteriaQuery[X], root: Root[T]) {

  private def path[Y](ps: Seq[String]): Path[Y] = {
    if (ps.isEmpty) {
      root.asInstanceOf[Path[Y]]
    }
    else {
      var join: Path[Y] = null
      for (i <- 0 until ps.size) {
        if (i == 0) join = root.get(ps(0))
        else join = join.get(ps(i))
      }
      join
    }
  }

  def ==(ps: Seq[String])(attrName: String, attrVal: Any): Predicate = {
    builder.equal(path(ps).get(attrName), attrVal)
  }

  def ==(attrName: String, attrVal: Any): Predicate = {
    ==(Nil)(attrName, attrVal)
  }

  def !=(ps: Seq[String])(attrName: String, attrVal: Any): Predicate = {
    builder.notEqual(path(ps).get(attrName), attrVal)
  }

  def !=(attrName: String, attrVal: Any): Predicate = {
    !=(Nil)(attrName, attrVal)
  }

  def <<(ps: Seq[String])(attrName: String, attrVal: Number): Predicate = {
    builder.lt(path(ps).get(attrName), attrVal)
  }

  def <<(attrName: String, attrVal: Number): Predicate = {
    <<(Nil)(attrName, attrVal)
  }

  def >>(ps: Seq[String])(attrName: String, attrVal: Number): Predicate = {
    builder.gt(path(ps).get(attrName), attrVal)
  }

  def >>(attrName: String, attrVal: Number): Predicate = {
    >>(Nil)(attrName, attrVal)
  }

  def <=(ps: Seq[String])(attrName: String, attrVal: Number): Predicate = {
    builder.le(path(ps).get(attrName), attrVal)
  }

  def <=(attrName: String, attrVal: Number): Predicate = {
    <=(Nil)(attrName, attrVal)
  }

  def >=(ps: Seq[String])(attrName: String, attrVal: Number): Predicate = {
    builder.ge(path(ps).get(attrName), attrVal)
  }

  def >=(attrName: String, attrVal: Number): Predicate = {
    >=(Nil)(attrName, attrVal)
  }

  def like(ps: Seq[String])(attrName: String, attrVal: String): Predicate = {
    builder.like(path(ps).get(attrName).as(classOf[String]), attrVal)
  }

  def like(attrName: String, attrVal: String): Predicate = {
    like(Nil)(attrName, attrVal)
  }

  def notLike(ps: Seq[String])(attrName: String, attrVal: String): Predicate = {
    builder.notLike(path(ps).get(attrName).as(classOf[String]), attrVal)
  }

  def notLike(attrName: String, attrVal: String): Predicate = {
    notLike(Nil)(attrName, attrVal)
  }

  def isNull(ps: Seq[String])(attrName: String): Predicate = {
    builder.isNull(path(ps).get(attrName))
  }

  def isNull(attrName: String): Predicate = {
    isNull(Nil)(attrName)
  }

  def isNotNull(ps: Seq[String])(attrName: String): Predicate = {
    builder.isNotNull(path(ps).get(attrName))
  }

  def isNotNull(attrName: String): Predicate = {
    isNotNull(Nil)(attrName)
  }

  def in(ps: Seq[String])(attrName: String, params: Seq[Any]): Predicate = {
    val ops = new java.util.ArrayList[Any]()
    params.foreach {
      it =>
        ops.add(it)
    }
    builder.isTrue(path(ps).get(attrName).in(ops))
  }

  def in(attrName: String, params: Seq[Any]): Predicate = {
    in(Nil)(attrName, params)
  }

  def not(ps: Predicate): Predicate = {
    builder.not(ps)
  }

  def asc(ps: Seq[String])(attrName: String): Order = {
    builder.asc(path(ps).get(attrName))
  }

  def asc(attrName: String): Order = {
    asc(Nil)(attrName)
  }

  def desc(ps: Seq[String])(attrName: String): Order = {
    builder.desc(path(ps).get(attrName))
  }

  def desc(attrName: String): Order = {
    desc(Nil)(attrName)
  }
}
