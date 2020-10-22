lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """stord-url-shortener""",
    version := "0.0.1",
    scalaVersion := "2.13.3",
    libraryDependencies ++= Seq(
      guice,
      "com.typesafe.play" %% "play-slick" % "5.0.0",
      "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
      "com.adrianhurt" %% "play-bootstrap" % "1.6.1-P28-B4",
      "com.h2database" % "h2" % "1.4.199",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test",
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )
