package org.koala.sporm.jpa

import javax.persistence.criteria.Path
import javax.persistence.criteria.{Root, CriteriaBuilder, Predicate}

case class CQExpression[T](builder: CriteriaBuilder, root: Root[T]) {

  import javax.persistence.criteria.Order


  private def path[Y](ps: Seq[String]): Path[Y] = {
    var join: Path[Y] = null
    for (i <- 0 until ps.size) {
      if (i == 0) join = root.get(ps(0))
      else join = join.get(ps(i))
    }
    join
  }

  def ==(attrName: String, attrVal: Any): Predicate = {
    ==(Nil)(attrName, attrVal)
  }

  def ==(ps: Seq[String])(attrName: String, attrVal: Any): Predicate = {
    builder.equal(path(ps).get(attrName), attrVal)
  }

  def !=(attrName: String, attrVal: Any): Predicate = {
    builder.notEqual(root.get(attrName), attrVal)
  }

  def <<(attrName: String, attrVal: Number): Predicate = {
    builder.lt(root.get(attrName), attrVal)
  }

  def >>(attrName: String, attrVal: Number): Predicate = {
    builder.gt(root.get(attrName), attrVal)
  }

  def <=(attrName: String, attrVal: Number): Predicate = {
    builder.le(root.get(attrName), attrVal)
  }

  def >=(attrName: String, attrVal: Number): Predicate = {
    builder.ge(root.get(attrName), attrVal)
  }

  def join[Y](joinName: String, attrName: String, attrVal: Any)(call: (CriteriaBuilder, Path[Y], Any) => Predicate): Predicate = {
    call(builder, root.get(joinName), attrVal)
  }

  def asc(attrName: String): Order = {
    builder.asc(root.get(attrName))
  }

  def desc(attrName: String): Order = {
    builder.desc(root.get(attrName))
  }

  def like(attrName: String, attrVal: String): Predicate = {
    builder.like(root.get(attrName).as(classOf[String]), attrVal)
  }

  def notLike(attrName: String, attrVal: String): Predicate = {
    builder.notLike(root.get(attrName).as(classOf[String]), attrVal)
  }

  def isNull(attrName: String): Predicate = {
    builder.isNull(root.get(attrName))
  }

  def isNotNull(attrName: String): Predicate = {
    builder.isNotNull(root.get(attrName))
  }

  def in(attrName: String, params: List[AnyRef]): Predicate = {
    builder.isTrue(root.get(attrName).in(params))
  }

  def not(ps: Predicate): Predicate = {
    builder.not(ps)
  }
}
