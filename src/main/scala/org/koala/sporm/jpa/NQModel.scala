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

  def fetch(qs: String, pm: Map[String, Any], limit: Int, offset: Int): Option[List[T]] = {
    withEntityManager {
      em =>
        _fetch(em.createQuery(qs, getType), pm, limit, offset)
    }
  }

  def fetch(qs: String, pm: Map[String, Any]): Option[List[T]] = {
    fetch(qs, pm, -1, -1)
  }

  def single(qs: String, pm: Map[String, Any]): Option[T] = {
    withEntityManager {
      em =>
        _single(em.createQuery(qs, getType), pm)
    }
  }

  def count(qs: String, pm: Map[String, Any]): Option[Long] = {
    withEntityManager {
      em =>
        _count(em.createQuery(qs, getType), pm)
    }
  }

  def count(qs: String): Option[Long] = {
    count(qs, Map())
  }

  def multi(qs: String, pm: Map[String, Any]): Option[List[Array[Any]]] = {
    withEntityManager {
      em =>
        _multi(em.createQuery(qs, classOf[List[Array[Any]]]), pm)
    }
  }

  def multi(qs: String): Option[AnyRef] = {
    multi(qs, Map())
  }

  def fetchByName(name: String, pm: Map[String, Any], limit: Int, offset: Int): Option[List[T]] = {
    withEntityManager {
      em =>
        _fetch(em.createNamedQuery(name, getType), pm, limit, offset)
    }
  }

  def fetchByName(name: String, pm: Map[String, Any]): Option[List[T]] = {
    fetchByName(name, pm, -1, -1)
  }

  def singleByName(name: String, pm: Map[String, Any]): Option[T] = {
    withEntityManager {
      em =>
        _single(em.createNamedQuery(name, getType), pm)
    }
  }

  def countByName(name: String, pm: Map[String, Any]): Option[Long] = {
    withEntityManager {
      em =>
        _count(em.createNamedQuery(name, getType), pm)
    }
  }

  def countByName(name: String): Option[Long] = {
    countByName(name, Map())
  }

  def multiByName(name: String, pm: Map[String, Any]): Option[List[Array[Any]]] = {
    withEntityManager {
      em =>
        _multi(em.createNamedQuery(name, classOf[List[Array[Any]]]), pm)
    }
  }

  def multiByName(qs: String): Option[AnyRef] = {
    multiByName(qs, Map())
  }
}