package ${data.pkg}

import io.sinq.provider._
import io.sinq.provider.jpa.{JpaAdapter, WhereImpl, FromImpl}
/**
* ########
* <p>NOT EDIT,The code generate</p>
* @author ya_feng_li@163.com
* ########
*/
case class SinqStream(val persistenceName: String = "default") extends JpaAdapter {

    def from[T](t: Table[T]): Where[T] = {
        val info = QueryLink(this)
        info.fromTables += t
        WhereImpl(info)
    }

    <#list data.ts as s>
    def select[${s.tpe}](${s.cols}): From[(${s.tpe})] = {
        val info = QueryLink(this)
        <#list s.cs as c>
        info.selectFields += ${c.name}
        </#list>
        FromImpl[(${s.tpe})](info)
    }
    </#list>
}