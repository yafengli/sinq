## 使用指南
+ JPA Entity(User.scala):

        import javax.persistence._
        import scala.beans.BeanProperty

        @Entity
        @Table(name = "t_user")
        case class User(@BeanProperty var name: String, @BeanProperty var age: Int, @BeanProperty var address: String) {
            @Id
            @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_user")
            @TableGenerator(name = "seq_t_user", table = "seq_t_user", allocationSize = 1)
            @BeanProperty
            var id: Long = _

            override def toString = id + ":" + name + ":" + age + ":" + address
        }
        
+ 创建`Table`对象：

        object _USER extends Table[User]("t_user","u") {
          def id = Column(this,"id")
          def name = Column(this,"name")
          def age = Column(this,"age")
          def address = Column(this,"address")
        }
        
+ 初始化数据源：`JPA.initPersistenceName(pns:String)`
+ 创建stream：`val sinq = SinqStream("h2")`

## Single(单对象查询)
+ JPA Entity结果集：

        sinq.from(_USER).where(Eq(User.id,1)).single(classOf[User]) match {
          case Some(u) => println(s"id:${u.id} name:${u.name}")
          case None => println("No Entity found.")
        }

+ 非JPA Entity结果集：

        sinq.select(_USER.id,_USER.name).from(_USER).where(Eq(User.id,1)).single() match {
          case Some(Array(id,name)) => println(s"id:${id} name:${name}")
          case None => println("No Entity found.")
        }
        sinq.select(_USER.id).from(_USER).where(Eq(User.id,1)).single() match {
          case Some(id) => println(s"id:${id}")
          case None => println("No Entity found.")
        }

## Collect(多对象查询)
+ JPA Entity结果集：

        sinq.from(_USER).where(Ge(User.id,1)).collect(classOf[User]).foreach { u => println(s"id:${u.id} name:${u.name}") }

+ 非JPA Entity结果集：

        sinq.select(_USER.id,_USER.name).from(_USER).where(Ge(User.id,1)).collect().foreach { case Array(id,name) => println(s"id:${id} name:${name}") }
        sinq.select(_USER.id).from(_USER).where(Ge(User.id,1)).collect().foreach { id => println(s"id:${id}") }

## SqlBuilder(拼接SQL)

        sinq.select(_USER.id,Sum(_USER.age))
            .from(_USER)
            .join(ADDRESS)
            .on(Eq(_USER.a_id,ADDRESS.id))
            .where(Ge(_USER.id,1))
            .groupBy(Le(_USER.id,10),_USER.id).having
            .orderBy(Order(ASC, _USER.id)).limit(10,0)
            .sql()

+ SQL: __select u.id,sum(u.age) from t_user t inner join t_address a on t.a_id = a.id where u.id >= ? group by u.id having u.id <= ? order by u.id asc limit 10 offset 0__

## 语法糖(CRUD)
+ 新增:`sinq.insert(t:Entity)`
+ 查找:`find[T](Any,Class[T])`
+ 删除:`sinq.delete(t:Entity)`
+ 更新:`sinq.update(t:Entity)`