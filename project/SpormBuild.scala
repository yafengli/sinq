import sbt._
import sbt.Keys._

object SpormBuild extends Build {

  lazy val sporm = Project(
    id = "sporm",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "sporm",
      organization := "org.koala",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      resolvers ++= Seq(
        "Local Maven Repository" at "file:///e:/repository/",
        "247" at "http://221.231.148.247/nexus/content/groups/public/",        
        "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
      ),
      libraryDependencies ++= Seq(
        "org.hibernate" % "hibernate-entitymanager" % "4.1.9.Final",
        "com.jolbox" % "bonecp" % "0.7.1.RELEASE",
        "postgresql" % "postgresql" % "9.1-901.jdbc4" % "test",
        "org.specs2" %% "specs2" % "1.12.3" % "test",
        "junit" % "junit" % "4.10" % "test"
      )
    )
  )
}
