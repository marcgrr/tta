name := "tta"

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= List(
  "com.google.inject" % "guice" % "3.0",
  "javax.inject" % "javax.inject" % "1")