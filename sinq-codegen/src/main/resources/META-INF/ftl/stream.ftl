package ${data.pkg}

import io.sinq.provider._
import io.sinq.provider.jpa.{WhereImpl, FromImpl}
/**
* NOT EDIT,The code generate
*/
case class SinqStream(val persistenceName: String = "default") extends JPAProvider {

    def from[T](t: Table[T]): Where[T] = {
        val info = QueryInfo(this)
        info.fromTables += t
        WhereImpl(info)
    }

    <#list data.ts as s>
    def select[${s.tpe}](${s.cols}): From[(${s.tpe})] = {
        val info = QueryInfo(this)
        <#list s.cs as c>
        info.selectFields += ${c.name}
        </#list>
        FromImpl[(${s.tpe})](info)
    }
    </#list>
}