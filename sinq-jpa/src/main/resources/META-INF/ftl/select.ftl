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

    <#list data.selects as s>
    def select[${s.tpes}](<#list s.cs as c>${c.name}:Column[${c.tpe}],</#list>): From[${s.tpes}] = {
        val info = QueryInfo(this)
        <#list s.cs as c>
        info.selectFields += ${c.name}
        </#list>
        FromImpl[${s.tpes}](info)
    }
    </#list>
}

<#list ["1","2","3","5"] as x>
    ${x}
</#list>