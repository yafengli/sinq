package org.koala.sporm

object SinqStream {
  def select(): M1 = new M1 {}
}


trait MResult {
  def where(): MWhere = new MWhere {}
}

//select ... from
trait MWhere {
  def orderBy (): M3 = new M3 {}

  def limit(): M3 = new M3 {}

}

//where ...
trait M1 {
  def from(): M2 = new M2 {}
}

trait M2 {
  def leftJoin(tableName: String, on: MExpression): M3 = new M3 {}

  def rightJoin(tableName: String, on: MExpression): M3 = new M3 {}

  def fullJoin(tableName: String, on: MExpression): M3 = new M3 {}

  def where(tableName: String, on: MExpression): M3 = new M3 {}
}

trait M3

trait M4

trait M5

trait M6


trait MExpression