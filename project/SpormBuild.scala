import sbt.Keys._
import sbt._
import scala.io.Source

object SpormBuild extends Build {
  val reg = "(.+)=(.+)".r
  val ver = Source.fromFile(new File("version.properties")).getLines().map(it => {
    val m = reg.findFirstMatchIn(it).get
    (m.group(1), m.group(2))
  }).toMap

  lazy val sporm = Project(
    id = "sporm",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "sporm",
      organization := "org.koala",
      version := ver("prod"),
      scalaVersion := ver("scala"),
      publishTo := Some(Resolver.file("My local maven repo", file("d:/repository"))),
      resolvers ++= Seq(
        "Local Maven Repository" at "file:///d:/repository/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "247" at "http://221.231.148.247/nexus/content/groups/public/",
        "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
      ),
      libraryDependencies ++= Seq(
        "org.hibernate" % "hibernate-entitymanager" % ver("hibernate"),
        "com.jolbox" % "bonecp" % ver("bonecp"),
        "ch.qos.logback" % "logback-classic" % ver("logback"),
        "postgresql" % "postgresql" % "9.1-901.jdbc4" % "test",
        "org.specs2" %% "specs2" % "1.14" % "test",
        "junit" % "junit" % "4.10" % "test"
      )
    )
  )
}

