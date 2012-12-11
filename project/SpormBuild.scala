import sbt._
import sbt.Keys._

object SpormBuild extends Build {

  lazy val sporm = Project(
    id = "sporm",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "sporm",
      organization := "org.example",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0-RC3",
      resolvers ++= Seq(
        "Local Maven Repository" at "file:///e:/repository/",
        "247" at "http://221.231.148.247/nexus/content/groups/public/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
        "releases"  at "http://oss.sonatype.org/content/repositories/releases",
        "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
      ),
      libraryDependencies ++= Seq(
        "org.hibernate" % "hibernate-entitymanager" % "4.1.8.Final",
        "com.jolbox" % "bonecp" % "0.7.1.RELEASE",
        "postgresql" % "postgresql" % "9.1-901.jdbc4",
        "org.specs2" %% "specs2" % "1.13-SNAPSHOT"
      )
    )
  )
}
