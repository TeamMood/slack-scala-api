
name := "slack-scala-api"

organization := "com.teammood"

scalaVersion := "2.13.12"

crossScalaVersions := Seq("2.13.12", "3.3.1")

libraryDependencies ++= Seq(
    "org.playframework" %% "play-json" % "3.0.1",
    "org.specs2" %% "specs2-core" % "4.20.3" % "test",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.8" % "test" // for Json equality in tests
)

scalacOptions in Test ++= Seq("-Yrangepos") // for Specs2

ThisBuild / versionScheme := Some("early-semver")

releaseCrossBuild := true

publishMavenStyle := true
publishTo := Some("GitHub Package Registry" at "https://maven.pkg.github.com/TeamMood/slack-scala-api")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))