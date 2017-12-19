lazy val rxjavaVersion = "2.1.7"
lazy val scalatestVersion = "3.0.4"
lazy val scalaChart = "0.5.1"

lazy val `budget-planner` = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "hohonuuli",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "budget-planner",
    libraryDependencies ++= Seq(
      "com.github.wookietreiber" %% "scala-chart" % scalaChart,
      "io.reactivex.rxjava2" % "rxjava" % rxjavaVersion,
      "org.scalatest" %% "scalatest" % scalatestVersion % "test",
      "scilube" %% "scilube-core" % "2.0.4"
    ),
    resolvers ++= Seq(
      Resolver.mavenLocal,
      Resolver.bintrayRepo("hohonuuli", "maven")
    )
  )
