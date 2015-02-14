import Build._

name := "sporm"

organization := "org.koala"

version := $("prod")

scalaVersion := "2.11.4"

lazy val root = project.in(file("."))

libraryDependencies ++= Seq(
  "org.hibernate" % "hibernate-entitymanager" % $("hibernate"),
  "com.google.guava" % "guava" % $("guava") % "provided",
  "ch.qos.logback" % "logback-classic" % $("logback") % "provided",
  "org.hibernate" % "hibernate-c3p0" % $("hibernate") % "provided",
  "com.alibaba" % "support" % $("support") % "provided",
  "com.h2database" % "h2" % $("h2") % "test",
  "junit" % "junit" % $("junit") % "test",
  "org.scalatest" %% "scalatest" % $("scalatest") % "test"
)


