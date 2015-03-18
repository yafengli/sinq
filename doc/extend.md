## 总有考虑不到的情况
__A君：二货，Sinq搞不定这个SQL，哈哈哈！！__
+ `withEntityManager`:

    import scala.collection.JavaConversions._
    SinqStream().withEntityManager {
      em =>
        val query = em.createNativeQuery("这个SQL真的可以有")
        query.getResultList.toList
    }

+ `withTransaction`使用方法与`withEntityManager`相同。

## 对，还有多数据库
+ `val sinq = SinqStream(JPA.PERSISCTENCE.NAME)`当前创建sinq的线程使用`JPA.PERSISCTENCE.NAME`配置的数据库内容。
+ `persistence.xml`:

    <?xml version="1.0" encoding="UTF-8"?>
    <persistence version="2.0" xmlns="http://java.sun.com/xml/ns/persistence"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">
        <persistence-unit name="h2" transaction-type="RESOURCE_LOCAL">
            <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
            <class>models.User</class>
            <class>models.Address</class>
            <properties>
                <property name="hibernate.connection.provider_class" value="support.DruidConnectionProvider"/>
                <property name="driverClassName" value="org.h2.Driver"/>
                <property name="url" value="jdbc:h2:~/test"/>
                <property name="username" value="sa"/>
                <property name="password" value=""/>
                <!-- hibernate -->
                <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
                <property name="hibernate.hbm2ddl.auto" value="update"/>
            </properties>
        </persistence-unit>
        <persistence-unit name="postgres" transaction-type="RESOURCE_LOCAL">
            <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
            <mapping-file>META-INF/query-models.xml</mapping-file>
            <class>models.Teacher</class>
            <class>models.Husband</class>
            <class>models.Student</class>
            <properties>
                <property name="hibernate.connection.provider_class" value="support.DruidConnectionProvider"/>
                <property name="driverClassName" value="org.postgres.Driver"/>
                <property name="url" value="jdbc:postgres://localhost:5432/test"/>
                <property name="username" value="postgres"/>
                <property name="password" value=""/>
                <!-- hibernate -->
                <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQL9Dialect"/>
                <property name="hibernate.hbm2ddl.auto" value="update"/>
            </properties>
        </persistence-unit>
    </persistence>

+ 多数据库：

    SinqStream("h2").select().from()...         //使用h2定义的配置数据源

    SinqStream("postgres").select().from()...   //使用postgres定义的配置数据源
