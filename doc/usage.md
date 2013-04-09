####Init

+ 必须首先初始化Persistence:`JPA.initPersistenceName("persistence.name")`。
+ 初始化JPA的配置PersistenceName名称，在多线程多数据库的需求中可以在调用Sporm前，绑定当前线程使用的PersistenceName。

####JPA Entity扩展

+ 对于每一个JPA Entity需要定义其伴生对象并集成CQModel类，例如：`Book.scala`

        @Entity
        @Table(name="t_book)
        class Book(var name:String,var address:String,var age:Int){
            @Id
            @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_book")
            @TableGenerator(name = "seq_t_book", table = "seq_t_book", allocationSize = 1)
            var id: Long = _
            @ManyToOne(optional = false)
            @JoinColumn(name = "author_id")
            var author:Author = _
        }

        object Book extends CQModel[Book]

+ `Author.scala`

        @Entity
        @Table(name="t_author)
        class Author(var name:String){
            @Id
            @GeneratedValue(strategy = GenerationType.TABLE, generator = "seq_t_author")
            @TableGenerator(name = "seq_t_author", table = "seq_t_author", allocationSize = 1)
            var id: Long = _
            @OneToMany(cascade = Array(CascadeType.REMOVE), mappedBy = "author")
            var books: Set[Book] = new HashSet[Book]()
        }

        object Author extends CQModel[Author]


####基本调用
+ 查询：`val book=Book.get(1L)`。
+ 增加：`book.insert`。
+ 修改：`book.update`。
+ 删除：`book.delete`。

####扩展调用
+ 对象查询：`fetch`

        val limit = 10
        val offset = 20
        Book.fetch(limit,offset) {
            f => {
                val cb=f.builder
                val root=f.root
                f.!=("name","test").asc("name")
        } match {
            case Some(list) => list.foreach(println _)
            case None =>
        }
+ 结果数查询：`count`

        Book.count {
            f => {
                val cb=f.builder
                val root=f.root
                f.!=("name","test").asc("name")
        } match {
            case Some(count) => println count
            case None =>
        }

####QL讲解
+ 在`fetch`与`count`方法中有`call: (CriteriaQL[T]) => CriteriaQL[T]`高阶函数
+ 函数的参数与返回结果均为`Criteria Query`封装

+ 简单QL：`Book.fetch(10,20)(_.!=("age",12).asc("id")`则其生成的最终SQL类似：

        select b.* from t_book b from t.age != 12 order by id asc

+ 连接(Join)：

        Book.fetch(10,20) {
            f =>
                val builder = f.builder
                val root = f.root

                val join = root.join("company")
                val p1 = builder.equal(join.get("name"), "test")
                val p2 = builder.equal(join.get("age"), 15)
                f::=(p1,p2).asc("id")
        } match {
            case Some(list) => list.foreach(println _)
            case None =>
        }
+ 则最终生成SQL类似：

        select b.* from t_book b left join t_author a on b.author_id = a.id where a.name = 'test' and a.age = 15 order by b.id asc


