enablePlugins(GatlingPlugin)

name := "gatlingTester"

version := "0.1"

scalaVersion := "2.12.7"


libraryDependencies ++= {
  val nscalaTimeV = "3.0.1.1"
  Seq("io.gatling.highcharts" % "gatling-charts-highcharts" % nscalaTimeV,
    "io.gatling" % "gatling-test-framework" % nscalaTimeV,
    "io.circe" %% "circe-core" % "0.8.0",
    "io.circe" %% "circe-generic" % "0.8.0",
    "io.circe" %% "circe-parser" % "0.8.0",
    "commons-codec" % "commons-codec" % "1.9").map(_ % "test")
}
