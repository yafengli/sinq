import sbt._
import sbt.Keys._

object SpormBuild extends Build {

  resolvers ++= Seq(
    "Local Maven Repository" at "file:///e:/repository/",
    "247" at "http://221.231.148.247/nexus/content/groups/public/",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
  )

  libraryDependencies ++= Seq(
    "org.hibernate" % "hibernate-entitymanager" % "4.1.8.Final",
    "org.hibernate" % "hibernate-c3p0" % "4.1.8.Final",
    "postgresql" % "postgresql" % "9.1-901.jdbc4"
  )

  lazy val sporm = Project(
    id = "sporm",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "sporm",
      organization := "org.example",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2",
      // add other settings here
      resolvers ++= Seq(
        "Local Maven Repository" at "file:///e:/repository/",
        "247" at "http://221.231.148.247/nexus/content/groups/public/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
      ),
      libraryDependencies ++= Seq(
        "org.hibernate" % "hibernate-entitymanager" % "4.1.8.Final",
        "org.hibernate" % "hibernate-c3p0" % "4.1.8.Final",
        "postgresql" % "postgresql" % "9.1-901.jdbc4"
      )
    )
  )
}
