package test

import javax.persistence.criteria.{Root, CriteriaQuery, CriteriaBuilder}

/**
 * User: ya_feng_li@163.com
 * Date: 13-7-8
 * Time: 上午11:34
 */
case class TQExp[T,X](builder:CriteriaBuilder,query:CriteriaQuery[X],root:Root[T]) {

}

class TQExm {
  def single[T]():T = {

  }
}
