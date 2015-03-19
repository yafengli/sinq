import org.koala.sbt.SbtAppPlugin._
import Build._

lazy val root = project.in(file(".")).aggregate(sinq, sinq_example)

lazy val sinq = project.in(file("sinq")).settings(
  name := "sinq",
  organization := "org.koala",
  version := $("prod"),
  scalaVersion := $("scala"),
  libraryDependencies ++= Seq(
    "org.hibernate" % "hibernate-entitymanager" % $("hibernate"),
    "org.hibernate" % "hibernate-jpamodelgen" % $("hibernate"),
    "ch.qos.logback" % "logback-classic" % $("logback"),
    "com.alibaba" % "druid" % $("druid"),
    "org.hibernate" % "hibernate-c3p0" % $("hibernate") % "test",
    "com.h2database" % "h2" % $("h2") % "test",
    "org.postgresql" % "postgresql" % $("postgresql") % "test",
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  )
)

lazy val sinq_example = project.in(file("sinq-example")).dependsOn(sinq).settings(
  name := "sinq-exmple",
  organization := "org.koala",
  version := $("prod"),
  scalaVersion := $("scala"),
  libraryDependencies ++= Seq(
    "org.jinq" % "jinq-jpa-scala" % $("jinq"), //jinq
    "com.google.guava" % "guava" % $("guava"),
    "com.alibaba" % "druid" % $("druid") % "test",
    "com.h2database" % "h2" % $("h2") % "test",
    "org.postgresql" % "postgresql" % $("postgresql") % "test",
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  )
).settings(appSettings: _*).settings(prefix := "sinq_example-" + $("prod"),dirSetting ++= Seq("doc" -> "doc"))
