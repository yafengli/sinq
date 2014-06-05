import sbt.Keys._
import sbt._
import scala.io.Source
import net.virtualvoid.sbt.graph.Plugin._

object SpormBuild extends Build {
  val reg = "(.+)=(.+)".r
  val $ = Source.fromFile(new File("version.properties")).getLines().map(it => {
    val m = reg.findFirstMatchIn(it).get
    (m.group(1), m.group(2))
  }).toMap

  lazy val sporm = Project(
    id = "sporm",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "sporm",
      organization := "org.koala",
      version := $("prod"),
      scalaVersion := "2.11.1",
      resolvers ++= Seq(
        "Local Maven Repository" at "file:///f:/repository/",
        "OSC Nexus" at "http://maven.oschina.net/content/groups/public/"
      ),
      publishTo := Some(Resolver.file("file", new File("d:/repository"))),
      libraryDependencies ++= Seq(
        "org.hibernate" % "hibernate-entitymanager" % $("hibernate"),
        "com.google.guava" % "guava" % $("guava") % "provided",
        "ch.qos.logback" % "logback-classic" % $("logback") % "provided",
        "org.hibernate" % "hibernate-c3p0" % $("hibernate") % "test",
        "com.alibaba" % "druid" % $("druid") % "test",
        "com.h2database" % "h2" % $("h2") % "test",
        "postgresql" % "postgresql" % "9.1-901.jdbc4" % "test",
        "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test",
        "junit" % "junit" % "4.10" % "test"
      )
    )
  ) settings (graphSettings: _*)
}

