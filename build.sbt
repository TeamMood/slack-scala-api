name := "slack-scala-api"

organization := "com.teammood"

version := "0.9.2-SNAPSHOT"

crossScalaVersions := Seq("2.11.8", "2.12.7", "2.13.1")

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.7.4"
)


bintrayOrganization := Some("teammood")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))