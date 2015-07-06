## 总有考虑不到的情况
__A君：二货，Sinq搞不定这个SQL，哈哈哈！！__
----------------------
+ `withEntityManager`:

        import scala.collection.JavaConversions._
        sinq.withEntityManager {
          em =>
            val query = em.createNativeQuery("这个SQL真的可以有")
            query.getResultList.toList
        }

+ `withTransaction`使用方法与`withEntityManager`相同。

__B君：二货，Sinq提供的API没有我的流弊，哈哈哈！！__
----------------------
+ 使用隐式导入，自由创建扩展：

        import io.sinq.SinqStream

        object ImplicitsSinq {
          implicit def sinq2Count(sinq: SinqStream) = new SinqStreamExtend(sinq)
        }       
        class SinqStreamExtend(val sinq: SinqStream) {
          def count[T](t: Class[T]): Long = {
            sinq.withEntityManager {
              em =>
                val query = em.createQuery(s"select count(t) from ${t.getName} t", classOf[java.lang.Long])
                query.getSingleResult.longValue()
              } getOrElse 0
            }
        }

+ 则`SinqStream`拓展了`count[T](Class[T])`方法：

        import init.ImplicitsSinq.sinq2Count
        val count = sinq.count(classOf[User])

