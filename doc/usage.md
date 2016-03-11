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

        object T_USER extends Table[User]("t_user") {
          def id = column("id", classOf[Long])
          def name = column("name", classOf[String])
          def age = column("age", classOf[Int])
          def address = column("address", classOf[String])
        }

+ 创建stream：`implicit val sinq = SinqStream("h2")`，`"h2"`是`JPA persistence.xml`中配置的`persistence-unit name`。

## Single(单对象查询)
+ JPA Entity结果集：

        sinq.from(T_USER).where(Eq(User.id,1)).single(classOf[User]) match {
          case Some(u) => println(s"id:${u.id} name:${u.name}")
          case None => println("No Entity found.")
        }

+ 非JPA Entity结果集：

        sinq.select(T_USER.id,T_USER.name).from(T_USER).where(Eq(User.id,1)).single() match {
          case Some(Array(id,name)) => println(s"id:${id} name:${name}")
          case None => println("No Entity found.")
        }
        sinq.select(T_USER.id).from(T_USER).where(Eq(User.id,1)).single() match {
          case Some(id) => println(s"id:${id}")
          case None => println("No Entity found.")
        }

## Collect(多对象查询)
+ JPA Entity结果集：

        sinq.from(T_USER).where(Ge(User.id,1)).collect(classOf[User]).foreach { u => println(s"id:${u.id} name:${u.name}") }

+ 非JPA Entity结果集：

        sinq.select(T_USER.id,T_USER.name).from(T_USER).where(Ge(User.id,1)).collect().foreach { case Array(id,name) => println(s"id:${id} name:${name}") }
        sinq.select(T_USER.id).from(T_USER).where(Ge(User.id,1)).collect().foreach { id => println(s"id:${id}") }

## SqlBuilder(拼接SQL)

        sinq.select(T_USER.id,Sum(T_USER.age))
            .from(T_USER)
            .join(ADDRESS)
            .on(Eq(T_USER.a_id,ADDRESS.id))
            .where(Ge(T_USER.id,1))
            .groupBy(Le(T_USER.id,10),T_USER.id).having
            .orderBy(Order(ASC, T_USER.id)).limit(10,0)
            .sql()

+ SQL: __select u.id,sum(u.age) from t_user t inner join t_address a on t.a_id = a.id where u.id >= ? group by u.id having u.id <= ? order by u.id asc limit 10 offset 0__

## 语法糖(CRUD)
+ 新增:`sinq.insert(t:Entity)`
+ 查找:`find[T](Any,Class[T])`
+ 删除:`sinq.delete(t:Entity)`
+ 更新:`sinq.update(t:Entity)`
