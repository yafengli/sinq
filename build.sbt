import Build._

scalaVersion := $("scala")

lazy val sinq = project.in(file(".")).aggregate(sinq_jpa, sinq_codegen, sinq_example)

lazy val sinq_jpa = project.in(file("sinq-jpa")).settings(
  name := "sinq-jpa",
  organization := "io.sinq",
  version := $("prod"),
  scalaVersion := $("scala"),
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % $("logback"),
    //"org.eclipse.persistence" % "eclipselink" % $("eclipselink") % "optional",
    "org.hibernate" % "hibernate-entitymanager" % $("hibernate"),
    "javax.transaction" % "jta" % $("jta"),
    "com.zaxxer" % "HikariCP" % $("HikariCP") % "test",
    "com.h2database" % "h2" % $("h2") % "test",
    "org.postgresql" % "postgresql" % $("postgresql") % "test",
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  ))

lazy val sinq_codegen = project.in(file("sinq-codegen")).enablePlugins(SbtDistApp).settings(
  name := "sinq-codegen",
  organization := "io.sinq",
  version := $("prod"),
  scalaVersion := $("scala"),
  mainClass := Some("io.sinq.codegen.GenerationBoot"),
  libraryDependencies ++= Seq(
    "org.freemarker" % "freemarker" % $("freemarker"),
    "org.hibernate" % "hibernate-entitymanager" % $("hibernate"),    
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  ))

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
    "com.h2database" % "h2" % $("h2") % "test",
    "org.postgresql" % "postgresql" % $("postgresql") % "test",
    "junit" % "junit" % $("junit") % "test",
    "org.scalatest" %% "scalatest" % $("scalatest") % "test"
  ))
