name := "slack-scala-api"

organization := "com.teammood"

version := "0.9.4-SNAPSHOT"

crossScalaVersions := Seq("2.12.7", "2.13.1")

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.7.4",
    "org.specs2" %% "specs2-core" % "4.6.0" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos") // for Specs2

bintrayOrganization := Some("teammood")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))