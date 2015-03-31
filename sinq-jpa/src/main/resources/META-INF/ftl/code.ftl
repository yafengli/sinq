package ${data.pkg}

import io.sinq._

object ${data.name} extends Table[${data.classname}]("${data.tablename}"){
    <#list data.fields as f>
    def ${f.name} = Column(this,classOf[${f.typename?default("HHH")}],"${f.columnId?default("AAA")}")
    </#list>

    <#assign flag=false/>
    def * = Seq(<#list data.fields as f><#if flag>,<#else><#assign flag=true/></#if>${f.name}</#list>)
}