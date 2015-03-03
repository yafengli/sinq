Sinq
=====
Sinq is a very simple scalable Object/Relation Mapping library for Java Persistence API.

目标
====
1.使用SQL；
2.使用JPA；
3.支持Scala和Java；
4.提供Functional链式操作；

#### 目标
+ 完整支持SQL语句；

#### 增删改
+ insert
+ delete
+ update

#### 查询结果(Query)
+ 单结果single()
+ 多结果collect()

#### JPA Entity
+ 查询：`select(classOf[T]).single()`与`select(classOf[T]).collect()`



#### NoJPA Entity
+ 查询：`select(column("col_1","col_2")).single()`与`select(column("col_1","col_2")).collect()`

#### where
+ `and`与`or`连接`expression`
+ 例如：

        ge("age",1).and(eq("name","John"))                                          //表达式 age >= 1 and name = 'John'
        ge("age",1).and(eq("name","John").or(eq("id",5L)))                          //表达式 age >= 1 and (name = 'John' or id = 5)
        gt("age",1).and(eq("name","John").or(ge("id",5L).and(le("id",10L))))        //表达式 age > 1 and (name = 'John' or (id > 5 and id < 10)

+ `gt`/`ge`/`lt`/`le`/`in`/`eq`/`between`等

#### 函数
+ `count`,`avg`,`sum`,`min`,`max`,`first`,`last`:使用SQL函数
+ `select(column(USER.ID,USER.NAME).single()`:单对象(NoEntity)查询single：
+ `collect`:多对象(Entity/Array[Object])查询
+ `limit、groupBy、orderBy、join、having on`
+ `query(sql:String):Seq[Array[AnyRef]]`:执行sql查询
+ `execute(sql:String):Int`:执行sql

##### 获取SQL字符串
+ 获取SQL字符串`.sql()`

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

        object USER {
            val ID = "id"
            val NAME = "name"
            val ADDRESS="address"
            val AGE = "age"
        }

        object IUser extends JPA{
          implicit def stream(a: User) = new SinqStream[User] {}
        }

+ Import Library:

        import IUser._

+ Simple Usage:

        val user = new User("name",10)
        insert(user)
        delete(user)
        update(user)

+ Custom Usage:

        val user = select().from(USER).where().groupBy().single()
        val count = select(Count(USER.ID),Column(USER.ID,USER.NAME),Sum(USER.ID).from(USER).where().single()

+ Complete Usage:

        val query = select(Column(USER.ID,USER.NAME)).from(USER).leftJoin(BOOK).on(Eq(USER.ID,BOOK.UID)).where(Eq(USER.NAME,"123").and(Ge(USER.AGE,12).or(Eq(USER.ADDRESS,"NJ"))))

        query.single()    //Option[AnyRef]
        query.collect()   //Iterator[AnyRef]
        query.sql()       //SQL string

