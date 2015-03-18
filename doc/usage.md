## SinqStream
+ 创建JPA Entity:
+ User.scala:

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

    object USER extends Table("t_user","u") {
      def id = Column(this,"id")
      def name = Column(this,"name")
      def age = Column(this,"age")
      def address = Column(this,"address")

      def * = Column(this,"id","name","address")
    }

+ `val sinq = SinqStream()`

## Single
+ JPA Entity结果集：

    sinq.select().from(USER).where(Eq(User.id,1)).single(classOf[User]) match {
      case Some(u) => println(s"id:${u.id} name:${u.name}")
      case None => println("No Entity found.")
    }

+ 非JPA Entity结果集：

    sinq.select(USER.id,USER.name).from(USER).where(Eq(User.id,1)).single() match {
      case Some(Array(id,name)) => println(s"id:${id} name:${name}")
      case None => println("No Entity found.")
    }

    sinq.select(USER.id).from(USER).where(Eq(User.id,1)).single() match {
      case Some(id) => println(s"id:${id}")
      case None => println("No Entity found.")
    }

## Collect
+ JPA Entity结果集：

    sinq.select().from(USER).where(Ge(User.id,1)).collect(classOf[User]).foreach { u => println(s"id:${u.id} name:${u.name}") }

+ 非JPA Entity结果集：

    sinq.select(USER.id,USER.name).from(USER).where(Ge(User.id,1)).collect().foreach { case Array(id,name) => println(s"id:${id} name:${name}") }

    sinq.select(USER.id).from(USER).where(Ge(User.id,1)).collect().foreach { id => println(s"id:${id}") }

## 当作SqlBuilder使用
+ `val query = sinq.select(USER.id,Sum(USER.age)).from(USER).join(ADDRESS).on(Eq(USER.a_id,ADDRESS.id)).where(Ge(USER.id,1)).groupBy(Le(USER.id,10),USER.id).orderBy(Order(ASC, USER.id)).limit(10,0)`
+ 获取SQL字符串：`query.sql()`:`select u.id,sum(u.age) from t_user t inner join t_address a on t.a_id = a.id where u.id >= 1 group by u.id having u.id <= 10 order by u.id asc limit 10 offset 0`
