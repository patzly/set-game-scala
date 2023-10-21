lazy val root = project
  .in(file("."))
  .settings(
    name := "SET Game",
    version := "0.1.0",

    scalaVersion := "3.3.1",

    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.16",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
  )
