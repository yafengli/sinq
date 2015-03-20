Sinq
====
Sinq is a very simple scalable Object/Relation Mapping library for Java Persistence API.

目标
====
>1. 遵循__JPA__规范
2. 使用[__SQL__](http://www.w3school.com.cn/sql/)
3. 支持[__Scala__](http://www.scala-lang.ort)
4. 提供直观的__Linq(Language Integrated Query)__式__Functional Chain__操作  

## Linq(Language Integrated Query)目标
+ 目标SQL:`select u.id from t_user u left join t_address a on u.id = a.u_id where u.id = ?1 group by u.id having u.id = ?2 order by u.id asc limit 10 offset 0`
+ 函数调用:

        select(USER.id).from(USER)
                       .leftJoin(ADDRESS).on(Eq(USER.id,ADDRESS.u_id))
                       .where(Eq(USER.id,1)
                       .groupBy(USER.id).having(Eq(USER.id,1))
                       .orderBy(Order(ASC, USER.id)).limit(10, 0)

## 使用指南
+ 初始化：`JPA.initPersistenceName(pns:String*)`
+ 创建：`val sinq = SinqStream("persistenceName")`
+ 使用：`sinq.select(...)...`

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

#### where语句
+ `and`与`or`连接`condition`
+ `gt`/`ge`/`lt`/`le`/`in`/`eq`/`between`等

#### 基本接口
+ 实现了`count`,`avg`,`sum`,`min`,`max`,`first`,`last`等基本SQL函数
+ 实现了`limit`、`groupBy`、`orderBy`、`join`、`having on`
+ 提供助手接口`withEntity`与`withTransaction`
+ 获取SQL字符串`sql()`
+ 获取参数集合：`params()`

#### 例子
+ `User.scala`

        @Entity
        @Table(name = "t_user")
        case class User(@BeanProperty var name: String, @BeanProperty var age: Int) {
          @Id
          @GeneratedValue(strategy = GenerationType.AUTO)
          @BeanProperty
          var id: Long = _

          @OneToOne(cascade = Array(CascadeType.REMOVE), mappedBy = "user")
          @BeanProperty
          var address: Address = _

          def this() = this(null, -1)

          def this(name: String, age: Int, address: Address) = {
            this(name, age)
            this.address = address
          }
        }

+ `Address.scala`:

        @Entity
        @Table(name = "t_address")
        case class Address(@BeanProperty var name: String, @BeanProperty var num: Int) {
          @Id
          @GeneratedValue(strategy = GenerationType.AUTO)
          @BeanProperty
          var id: Long = _

          @OneToOne(fetch = FetchType.EAGER, optional = false)
          @JoinColumn(name = "u_id")
          @BeanProperty
          var user: User = _

          def this() = this(null, -1)

          def this(name: String, age: Int, user: User) = {
            this(name, age)
            this.user = user
          }
        }

+ 创建封装类：

        object USER extends Table("t_user","u") {
          val id = Column(this,"id")
          val name = Column(this,"name")
          val address = Column(this,"address")
          val age = Column(this,"age")

          def * = Column(this,"id","name","address","age") //or Seq(id,name,address,age)
        }

        object ADDRESS extends Table("t_address", "a") {
          def id = Column(this, "id")

          def name = Column(this, "name")

          def num = Column(this, "num")

          def u_id = Column(this, "u_id")

          def * = Seq(id, name, num)
        }

+ 初始化数据源:`JPA.initPersistenceName("h2")`

+ 创建全局对象

        implict val sinq = SinqStream("h2")

+ 基本应用:

        val user = new User("name",10)
        sinq.insert(user)
        sinq.delete(user)
        sinq.update(user)

+ 条件查询:

        sinq.select(classOf[User])
            .from(USER)
            .where()
            .groupBy()
            .orderBy(ASC)
            .limit(10)
            .offset(50)
            .single()

        sinq.select(Count(USER.id),Column(USER.id,USER.name),Sum(USER.id)
            .from(USER)
            .where()
            .groupBy()
            .orderBy(ASC)
            .limit(10)
            .offset(50)
            .single()

+ 结果集:

        val query = sinq.select(USER.id,USER.name)
                        .from(USER).leftJoin(ADDRESS)
                        .on(Eq(USER.id,ADDRESS.u_id))
                        .where(Eq(USER.id,1)
                        .orderBy(Order(ASC, USER.id)).limit(10, 0)

        query.single()
