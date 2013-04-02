import sbt._
import sbt.Keys._

object SpormBuild extends Build {

  lazy val sporm = Project(
    id = "sporm",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "sporm",
      organization := "org.koala",
      version := "0.1",
      scalaVersion := "2.10.1",
      publishTo := Some(Resolver.file("My local maven repo", file("d:/repository"))),
      resolvers ++= Seq(
        "Local Maven Repository" at "file:///d:/repository/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "247" at "http://221.231.148.247/nexus/content/groups/public/",
        "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
      ),
      libraryDependencies ++= Seq(
        "org.hibernate" % "hibernate-entitymanager" % "4.2.0.Final",
        "com.jolbox" % "bonecp" % "0.8.0-rc1",
        "ch.qos.logback" % "logback-classic" % "1.0.11",
        "postgresql" % "postgresql" % "9.1-901.jdbc4" % "test",
        "org.specs2" %% "specs2" % "1.14" % "test",
        "junit" % "junit" % "4.10" % "test"
      )
    )
  )
}
