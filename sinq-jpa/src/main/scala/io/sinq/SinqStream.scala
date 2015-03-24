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

  def select[T1, T2, T3, T4, T5, T6, T7](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7]): From[(T1, T2, T3, T4, T5, T6, T7)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    FromImpl[(T1, T2, T3, T4, T5, T6, T7)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8]): From[(T1, T2, T3, T4, T5, T6, T7, T8)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    info.selectFields += c14
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    info.selectFields += c14
    info.selectFields += c15
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    info.selectFields += c14
    info.selectFields += c15
    info.selectFields += c16
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    info.selectFields += c14
    info.selectFields += c15
    info.selectFields += c16
    info.selectFields += c17
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    info.selectFields += c14
    info.selectFields += c15
    info.selectFields += c16
    info.selectFields += c17
    info.selectFields += c18
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18], c19: Column[T19]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    info.selectFields += c14
    info.selectFields += c15
    info.selectFields += c16
    info.selectFields += c17
    info.selectFields += c18
    info.selectFields += c19
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18], c19: Column[T19], c20: Column[T20]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    info.selectFields += c14
    info.selectFields += c15
    info.selectFields += c16
    info.selectFields += c17
    info.selectFields += c18
    info.selectFields += c19
    info.selectFields += c20
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18], c19: Column[T19], c20: Column[T20], c21: Column[T21]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    info.selectFields += c14
    info.selectFields += c15
    info.selectFields += c16
    info.selectFields += c17
    info.selectFields += c18
    info.selectFields += c19
    info.selectFields += c20
    info.selectFields += c21
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)](info)
  }

  def select[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](c1: Column[T1], c2: Column[T2], c3: Column[T3], c4: Column[T4], c5: Column[T5], c6: Column[T6], c7: Column[T7], c8: Column[T8], c9: Column[T9], c10: Column[T10], c11: Column[T11], c12: Column[T12], c13: Column[T13], c14: Column[T14], c15: Column[T15], c16: Column[T16], c17: Column[T17], c18: Column[T18], c19: Column[T19], c20: Column[T20], c21: Column[T21], c22: Column[T22]): From[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] = {
    val info = QueryInfo(this)
    info.selectFields += c1
    info.selectFields += c2
    info.selectFields += c3
    info.selectFields += c4
    info.selectFields += c5
    info.selectFields += c6
    info.selectFields += c7
    info.selectFields += c8
    info.selectFields += c9
    info.selectFields += c10
    info.selectFields += c11
    info.selectFields += c12
    info.selectFields += c13
    info.selectFields += c14
    info.selectFields += c15
    info.selectFields += c16
    info.selectFields += c17
    info.selectFields += c18
    info.selectFields += c19
    info.selectFields += c20
    info.selectFields += c21
    info.selectFields += c22
    FromImpl[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)](info)
  }
}

