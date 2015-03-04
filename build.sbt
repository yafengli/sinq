import Build._

name := "sinq"

organization := "org.koala"

version := $("prod")

scalaVersion := $("scala")

libraryDependencies ++= Seq(
  "org.hibernate" % "hibernate-entitymanager" % $("hibernate"),
  "org.hibernate" % "hibernate-jpamodelgen" % $("hibernate"),
  "com.google.guava" % "guava" % $("guava") % "provided",
  "ch.qos.logback" % "logback-classic" % $("logback") % "provided",
  "org.hibernate" % "hibernate-c3p0" % $("hibernate") % "provided",
  "com.alibaba" % "druid" % $("druid") % "provided",
  "com.h2database" % "h2" % $("h2") % "test",
  "junit" % "junit" % $("junit") % "test",
  "org.scalatest" %% "scalatest" % $("scalatest") % "test"
)


