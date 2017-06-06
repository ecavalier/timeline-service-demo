name := "timeline-service"

version := "1.0"

lazy val `timeline-service` = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(JavaAppPackaging)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
//  jdbc ,
  cache ,
  ws   ,
  filters  ,
  "org.mindrot" % "jbcrypt" % "0.4",
  "org.json4s" %% "json4s-native" % "3.5.2"  ,
  "com.h2database" % "h2" % "1.4.195"  ,
  "com.typesafe.play" %% "play-slick" % "2.1.0"  ,
  "com.typesafe.play" %% "play-slick-evolutions" % "2.1.0"  ,
  "io.swagger" %% "swagger-play2" % "1.5.3",
  "org.webjars" % "swagger-ui" % "3.0.10" ,
  specs2 % Test
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
