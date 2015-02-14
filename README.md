sporm
=====
A very simple scalable Object/Relation Mapping library for Java Persistence API.

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

        `ge("age",1).and(eq("name","John"))`条件为`where age >= 1 and name = 'John'`
        `ge("age",1).and(eq("name","John").or(eq("id",5L)))`条件为`where age >= 1 and (name = 'John' or id = 5)`
        `gt("age",1).and(eq("name","John").or(ge("id",5L).and(le("id",10L))))`条件为`where age > 1 and (name = 'John' or (id > 5 and id < 10)`

+ `gt`/`ge`/`lt`/`le`/`in`/`eq`/`between`

#### 函数
+ `count`,`avg`,`sum`,

+ 单对象(NoEntity)查询single：`select(column("col_1","col_2").single()`
+ 多对象(Entity/Array[Object])查询collect
+ 数目查询count
+ limit、groupBy、orderBy、join、having on
+ 查询语句字符串sql
+ 执行sql查询query(sql:String):Seq[Array[AnyRef]]

####

##### 获取dml与ddl

+ 获取数据操纵字符串`.dml()`
+ 获取数据定义字符串`.ddl()`

#### 例子
+ Entity:

        User.scala

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
            var age:Int = _

            def this(id:Long,name:String,age:Int) = {
                this.name = name
                this.age = age
            }
        }

        object User extends CQModel

        //Usage:
        val user = new User("name",10)
        user.insert()
        user.delete()
        user.update()

        val user = JPA.select().where().groupBy().single().getOrElse(new User)
