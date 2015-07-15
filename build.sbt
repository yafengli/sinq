import Build._

resolvers += "jcenter" at "http://jcenter.bintray.com"

lazy val root = project.in(file(".")).aggregate(sinq_jpa, sinq_example)

lazy val sinq_jpa = project.in(file("sinq-jpa")).settings(
  name := "sinq-jpa",
  organization := "io.sinq",
  version := $("prod"),
  scalaVersion := $("scala"),
  libraryDependencies ++= Seq(
    "org.hibernate" % "hibernate-entitymanager" % $("hibernate"),
    "org.hibernate" % "hibernate-jpamodelgen" % $("hibernate"),
    "ch.qos.logback" % "logback-classic" % $("logback"),
    "com.zaxxer" % "HikariCP" % $("HikariCP") % "test",
    "com.alibaba" % "druid" % $("druid") % "test",
    "org.hibernate" % "hibernate-c3p0" % $("hibernate") % "test",
    "com.h2database" % "h2" % $("h2") % "test",
    "org.postgresql" % "postgresql" % $("postgresql") % "test",
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  )
)

lazy val sinq_codegen = project.in(file("sinq-codegen")).dependsOn(sinq_jpa).settings(
  name := "sinq-codegen",
  organization := "io.sinq",
  version := $("prod"),
  scalaVersion := $("scala"),
  libraryDependencies ++= Seq(
    "org.freemarker" % "freemarker" % $("freemarker"),
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  )
)

lazy val sinq_example = project.in(file("sinq-example")).dependsOn(sinq_jpa,sinq_codegen).settings(
  name := "sinq-example",
  organization := "io.sinq",
  version := $("prod"),
  scalaVersion := $("scala"),
  packageOptions in(Compile, packageBin) += Package.ManifestAttributes("Build" -> "WaHaYaWaHaYaWaHaHa"),
  packageOptions in(Compile, packageBin) += Package.ManifestAttributes("MainClass" -> "test.MainAppClass"),
    libraryDependencies ++= Seq(
    "com.google.guava" % "guava" % $("guava"),
    "com.zaxxer" % "HikariCP" % $("HikariCP") % "test",
    "com.alibaba" % "druid" % $("druid") % "test",
    "com.h2database" % "h2" % $("h2") % "test",
    "org.postgresql" % "postgresql" % $("postgresql") % "test",
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  ))
