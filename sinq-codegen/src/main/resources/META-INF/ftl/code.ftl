package ${data.pkg}

import io.sinq.provider._

object ${data.name} extends Table[${data.entityClassName}]("${data.tableName}"){
  <#list data.fields as f>
  def ${f.name} = column("${f.columnId}", classOf[${f.typename}])
  </#list>
}