Sinq
====
Sinq is a very simple scalable Object/Relation Mapping library for Java Persistence API.

目标
====
* 使用__JPA__；
* 使用__SQL__；
* 支持__Scala__；
* 提供直观的__Functional Chain__操作；

## 使用指南
+ 初始化：`JPA.initPersistenceName(pns:String*)`
+ 创建：`val sinq = SinqStream("persistenceName")`
+ 使用：`sinq.insert(entityObj)`

#### 增删改(CRUD)
+ `insert(Entity)`
+ `find[T](Any,Class[T])`
+ `delete(Entity)`
+ `update(Entity)`

#### 查询(Query)
+ `select(cols:Column)...`

#### 结果集
+ 单结果`single()`与`single[T](Class[T])`
+ 多结果`collect()`与`collect[T](Class[T])`

#### 结果集(Entity/Tuple)
+ JPA Entity:`select(classOf[T])`
+ Tuple:`select(Column(USER.ID,USER.NAME))`

#### select语句
* `select[T](ct:Class[T])`
* `select(columns:Column*)`

#### where语句
+ `and`与`or`连接`condition`
+ `gt`/`ge`/`lt`/`le`/`in`/`eq`/`between`等

#### 函数
+ `count`,`avg`,`sum`,`min`,`max`,`first`,`last`:使用SQL函数
+ `select(Column(USER.ID,USER.NAME).from(USER).where().single()`:单对象(NoEntity)查询single：
+ `collect`:多对象(Entity/Array[Object])查询
+ `limit、groupBy、orderBy、join、having on`
+ `query(sql:String):Seq[Array[AnyRef]]`:执行sql查询
+ `execute(sql:String):Int`:执行sql

##### 获取SQL字符串
+ 获取SQL字符串`sql()`
+ 获取参数集合：`params()`

#### 例子
+ Entity:`User.scala`

        @Entity
        @Table(name = "t_user")
        class User {
            @Id
            @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_book")
            @TableGenerator(name = "seq_t_book", table = "seq_t_book", allocationSize = 1)
            @BeanProperty
            var id: Long = _

            @BeanProperty
            @Column
            var name:String = _
            @BeanProperty
            @Column
            var address:String = _
            @BeanProperty
            @Column
            var age:Int = _

            def this(id:Long,name:String,age:Int) = {
                this.name = name
                this.age = age
            }
        }

        object USER extends Table {
            val ID = "id"
            val NAME = "name"
            val ADDRESS="address"
            val AGE = "age"

            override def tableName():String = "t_user"
        }

+ Import Library:

        import io.SinqStream._

        implict val stream = new SinqStream()

+ Simple Usage:

        val user = new User("name",10)
        stream.insert(user)
        stream.delete(user)
        stream.update(user)

+ Custom Usage:

        stream.select(classOf[User])
              .from(USER)
              .where()
              .groupBy()
              .orderBy(ASC)
              .limit(10)
              .offset(50)
              .single()

        stream.select(Count(USER.ID),Column(USER.ID,USER.NAME),Sum(USER.ID)
              .from(USER)
              .where()
              .groupBy()
              .orderBy(ASC)
              .limit(10)
              .offset(50)
              .single()

+ Complete Usage:

        val query = stream.select(Column(USER.ID,USER.NAME))
                          .from(USER).leftJoin(BOOK)
                          .on(Eq(USER.ID,BOOK.UID))
                          .where(Eq(USER.NAME,"123").and(Ge(USER.AGE,12).or(Eq(USER.ADDRESS,"NJ"))))
        query.sql()
        query.single()
        query.collect()
