name := """TitleBot"""
organization := "mitchell"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.mockito" % "mockito-all" % "1.8.4"
libraryDependencies += "org.jsoup" % "jsoup" % "1.14.3"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "mitchell.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "mitchell.binders._"
