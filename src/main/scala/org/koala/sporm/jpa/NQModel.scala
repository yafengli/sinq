package org.koala.sporm.jpa

/**
 * @author ya_feng_li@163.com
 * @since 1.0
 *        Name Query Model
 */
abstract class NQModel[T: Manifest] extends JPA with NQBuilder {
  def getType = implicitly[Manifest[T]].runtimeClass

  implicit def generateModel(entity: T) = new BaseBuilder[T](entity)

  def get(id: Any): Option[T] = {
    withEntityManager {
      _.find(getType, id).asInstanceOf[T]
    }
  }

  def fetch(qs: String, ops: Array[Any], limit: Int, offset: Int): Option[List[T]] = {
    withEntityManager {
      em =>
        _fetch(em.createQuery(qs, getType), ops, limit, offset)
    }
  }

  def fetch(qs: String, ops: Array[Any]): Option[List[T]] = {
    fetch(qs, ops, -1, -1)
  }

  def single(qs: String, ops: Array[Any]): Option[T] = {
    withEntityManager {
      em =>
        _single(em.createQuery(qs, getType), ops)
    }
  }

  def count(qs: String, ops: Array[Any]): Option[Long] = {
    withEntityManager {
      em =>
        _count(em.createQuery(qs, getType), ops)
    }
  }

  def count(qs: String): Option[Long] = {
    count(qs, Array())
  }

  def multi(qs: String, ops: Array[Any]): Option[List[Array[Any]]] = {
    withEntityManager {
      em =>
        _multi(em.createQuery(qs, classOf[List[Array[Any]]]), ops)
    }
  }

  def multi(qs: String): Option[AnyRef] = {
    multi(qs, Array())
  }

  def fetchByName(name: String, ops: Array[Any], limit: Int, offset: Int): Option[List[T]] = {
    withEntityManager {
      em =>
        _fetch(em.createNamedQuery(name, getType), ops, limit, offset)
    }
  }

  def fetchByName(name: String, ops: Array[Any]): Option[List[T]] = {
    fetchByName(name, ops, -1, -1)
  }

  def singleByName(name: String, ops: Array[Any]): Option[T] = {
    withEntityManager {
      em =>
        _single(em.createNamedQuery(name, getType), ops)
    }
  }

  def countByName(name: String, ops: Array[Any]): Option[Long] = {
    withEntityManager {
      em =>
        _count(em.createNamedQuery(name, getType), ops)
    }
  }

  def countByName(name: String): Option[Long] = {
    countByName(name, Array())
  }

  def multiByName(name: String, ops: Array[Any]): Option[List[Array[Any]]] = {
    withEntityManager {
      em =>
        _multi(em.createNamedQuery(name, classOf[List[Array[Any]]]), ops)
    }
  }

  def multiByName(qs: String): Option[AnyRef] = {
    multiByName(qs, Array())
  }
}