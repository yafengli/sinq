Sinq
====
在[__Scala__](http://www.scala-lang.org)的项目中支持集成[__JPA__](https://jcp.org/en/jsr/detail?id=338)，并提供类[__SQL__](http://www.w3school.com.cn/sql/)的函数调用。  

目标
====
+ 操作关系数据库应该使用 __SQL__ ，还没有过时不应该被抛弃；
+ 写出不容易错的 __SQL__ ，编译器能够理解的 __SQL__ ，开发工具能够友好玩耍的 __SQL__ ，而不是一坨又一坨的字符串；
+ 提供 __Linq(Language Integrated Query)__ 方式查询，良好直观的类似 __SQL__ 方式的 __Functional Chain__ 调用；
+ SQL字串:

        """SELECT u.id FROM t_user u
                  LEFT JOIN t_address a ON u.id = a.u_id
                  WHERE u.id = ?1
                  GROUP BY u.id HAVING u.id = ?2
                  ORDER BY u.id ASC LIMIT 10 OFFSET 0"""

+ 函数调用:

        select(_USER.id).from(_USER)
                     .leftJoin(_ADDRESS).on(Eq(_USER.id,_ADDRESS.u_id))
                     .where(Eq(_USER.id,1)
                     .groupBy(_USER.id).having(Eq(_USER.id,1))
                     .orderBy(Order(ASC, _USER.id)).limit(10, 0)

## 应用步骤
+ 初始化：

        val sinq = SinqStream("h2")                      //persistence name

+ 调用：

        sinq.insert(new User("test",12))              //插入
        sinq.select(_USER.id).from(_USER)             //查询

+ 销毁：

        JPA.releaseAll()

#### 增删改查(CRUD)
+ 新增`insert(Entity)`
+ 查找`find[T](Any,Class[T])`
+ 删除`delete(Entity)`
+ 更新`update(Entity)`

#### 查询(Query)
+ 查询`select(Column*)...`
+ 单结果`single()`
+ 多结果`collect()`

#### where语句
+ `and`与`or`连接`condition`
+ `gt`/`ge`/`lt`/`le`/`in`/`eq`/`between`等

#### 基本接口
+ 实现了`count`,`avg`,`sum`,`min`,`max`,`first`,`last`等基本SQL函数
+ 实现了`limit`、`groupBy`、`orderBy`、`join`、`having on`
+ 提供助手接口`withEntity`与`withTransaction`
+ 获取SQL字符串`sql()`
+ 获取参数集合`params()`

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

        object _USER extends Table[User]("t_user") {
            val id = Column(this,"id")
            val name = Column(this,"name")
            val address = Column(this,"address")
            val age = Column(this,"age")          
        }

        object _ADDRESS extends Table[Address]("t_address") {
            def id = Column(this, "id")
            def name = Column(this, "name")
            def num = Column(this, "num")
            def u_id = Column(this, "u_id")
        }

+ 初始化数据源与工具对象：
        
        JPA.initPersistenceName("h2")
        lazy val sinq = SinqStream("h2")

+ 基本应用:

        val user = new User("name",10)
        sinq.insert(user)
        sinq.delete(user)
        sinq.update(user)
                                
+ Entity查询：
        
        val query = sinq.from(_USER)
                        .where(Ge(_USER.age,6))
                        .orderBy(Order(ASC,_USER.id))
                        .limit(10,50)
        println(query.sql())     
        //select t1.* from t_user where t1.age >= 6 order by t1.id asc limit 10 offset 50
        
+ 非Entity查询:

        val query = sinq.select(_USER.id,_USER.name)
                        .from(_USER)
                        .where(Ge(_USER.age,6))
                        .orderBy(Order(ASC,_USER.id))
                        .limit(10,50)
                        .offset(50)
                                                
        println(query.sql())     
        //select t1.id as t1_id,t1.name as t1_name from t_user where t1.age >= 6 order by t1.id asc limit 10 offset 50
                 
+ 查询结果：
                        
        query.single()      //单个对象
        query.collect()     //对象集