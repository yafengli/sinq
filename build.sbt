import net.virtualvoid.sbt.graph.Plugin._

val reg = "(.+)=(.+)".r

lazy val $ = scala.io.Source.fromFile(new File("version.properties")).getLines().map(it => {
  val m = reg.findFirstMatchIn(it).get
  (m.group(1), m.group(2))
}).toMap

lazy val root = (project in file(".")).settings(graphSettings: _*)

name := "sporm"

version := $("prod")

scalaVersion := "2.11.1"

resolvers ++= Seq("Local Maven Repository" at "file:///f:/repository/", "OSC Nexus" at "http://maven.oschina.net/content/groups/public/")

libraryDependencies ++= Seq(
  "org.hibernate" % "hibernate-entitymanager" % $("hibernate"),
  "com.google.guava" % "guava" % $("guava") % "provided",
  "ch.qos.logback" % "logback-classic" % $("logback") % "provided",
  "org.hibernate" % "hibernate-c3p0" % $("hibernate") % "test",
  "com.alibaba" % "druid" % $("druid") % "test",
  "com.h2database" % "h2" % $("h2") % "test",
  "postgresql" % "postgresql" % "9.1-901.jdbc4" % "test",
  "junit" % "junit" % "4.10" % "test",
  "org.scalatest" %% "scalatest" % "2.1.3" % "test"
)


