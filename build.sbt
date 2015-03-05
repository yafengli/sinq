import Build._

lazy val sinq = project.in(file(".")).aggregate(sinq_core, sinq_example)

lazy val sinq_core = project.in(file("sinq")).settings(
  name := "sinq",
  organization := "org.koala",
  version := $("prod"),
  scalaVersion := $("scala"),
  libraryDependencies ++= Seq(
    "org.hibernate" % "hibernate-entitymanager" % $("hibernate"),
    "org.hibernate" % "hibernate-jpamodelgen" % $("hibernate"),
    "ch.qos.logback" % "logback-classic" % $("logback"),
    "org.hibernate" % "hibernate-c3p0" % $("hibernate") % "test",
    "com.alibaba" % "druid" % $("druid") % "test",
    "com.h2database" % "h2" % $("h2") % "test",
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  )
)

lazy val sinq_example = project.in(file("sinq-example")).dependsOn(sinq_core).settings(
  name := "sinq",
  organization := "org.koala",
  version := $("prod"),
  scalaVersion := $("scala"),
  libraryDependencies ++= Seq(
    "org.jinq" % "jinq-jpa-scala" % $("jinq"),//jinq
    "com.google.guava" % "guava" % $("guava"),
    "org.hibernate" % "hibernate-c3p0" % $("hibernate") % "test",
    "com.alibaba" % "druid" % $("druid") % "test",
    "com.h2database" % "h2" % $("h2") % "test",
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  )
)
