lazy val root = project
  .in(file("."))
  .settings(
    name := "SET Game",
    version := "1.0.0",

    scalaVersion := "3.3.1",

    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test",
    libraryDependencies += "org.scalatestplus" %% "mockito-5-8" % "3.2.17.0" % "test",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "com.google.inject" % "guice" % "6.0.0",
    libraryDependencies += "net.codingwell" %% "scala-guice" % "6.0.0",
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.2.0",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.3"
  )