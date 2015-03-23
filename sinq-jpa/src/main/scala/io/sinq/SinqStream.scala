package io.sinq

import io.sinq.provider._
import io.sinq.provider.jpa.FromImpl

case class SinqStream(val persistenceName: String = "default") extends JPAProvider {

  def select[T](t: Table[T]): From[T] = {
    val info = QueryInfo(this)
    info.setSelectTable(t)
    FromImpl[T](info)
  }

  def select[T1](c1: Column[T1]): From[T1] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    FromImpl[T1](info)
  }

  def select[T1, T2](c1: Column[T1], c2: Column[T2]): From[(T1, T2)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    FromImpl[(T1, T2)](info)
  }

  def select[T1, T2, T3](c1: Column[T1], c2: Column[T2], c3: Column[T3]): From[(T1, T2, T3)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    FromImpl[(T1, T2, T3)](info)
  }

  def select[T1, T2, T3, T4](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4]): From[(T1, T2, T3, T4)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    FromImpl[(T1, T2, T3, T4)](info)
  }

  def select[T1, T2, T3, T4, T5](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5]): From[(T1, T2, T3, T4, T5)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    FromImpl[(T1, T2, T3, T4, T5)](info)
  }

  def select[T1, T2, T3, T4, T5, T6](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6]): From[(T1, T2, T3, T4, T5, T6)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    FromImpl[(T1, T2, T3, T4, T5, T6)](info)
  }
}

