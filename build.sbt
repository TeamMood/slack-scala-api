name := "slack-scala-api"

organization := "com.teammood"

version := "0.9.0"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.7.4"
)


bintrayOrganization := Some("teammood")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))