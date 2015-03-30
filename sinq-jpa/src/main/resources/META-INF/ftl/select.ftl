package ${data.pkg}

import io.sinq.provider._
import io.sinq.provider.jpa.{WhereImpl, FromImpl}

case class SinqStream(val persistenceName: String = "default") extends JPAProvider {

    def from[T](t: Table[T]): Where[T] = {
        val info = QueryInfo(this)
        info.setSelectTable(t)
        info.fromTables += t
        WhereImpl(info)
    }

    def select[T](t: Table[T]): From[T] = {
        val info = QueryInfo(this)
        info.setSelectTable(t)
        FromImpl[T](info)
    }

    <#list data.teps as s>
    def select[${s.tpe}](<#list s.cs as c>${c.name}:Column[${c.tpe}],</#list>): From[${s.tpe}] = {
        val info = QueryInfo(this)
        <#list s.cs as c>
        info.selectFields += ${c.name}
        </#list>
        FromImpl[${s.tpe}](info)
    }
    </#list>
}