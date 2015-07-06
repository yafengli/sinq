package ${data.pkg}

import io.sinq._

object ${data.name} extends Table[${data.entityClassName}]("${data.tableName}"){
    <#list data.fields as f>
    def ${f.name} = Column(this,classOf[${f.typename}],"${f.columnId}")
    </#list>
}