
name := "slack-scala-api"

organization := "com.teammood"

version := "1.1.0"

scalaVersion := "2.13.12"

crossScalaVersions := Seq("2.13.12", "3.3.1")

libraryDependencies ++= Seq(
    "org.playframework" %% "play-json" % "3.0.1",
    "org.specs2" %% "specs2-core" % "4.20.3" % "test",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.8" % "test" // for Json equality in tests
)

scalacOptions in Test ++= Seq("-Yrangepos") // for Specs2

publishMavenStyle := true
publishTo := Some("mymavenrepo.com.write" at "https://mymavenrepo.com/repo/dukQxd8bxgHTFMPUdYTb/")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))