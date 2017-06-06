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
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.json4s" %% "json4s-native" % "3.2.11"  ,
  "com.h2database" % "h2" % "1.4.187"  ,
  "com.typesafe.play" %% "play-slick" % "1.1.1"  ,
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1"  ,
  "io.swagger" %% "swagger-play2" % "1.5.2",
  "org.webjars" % "swagger-ui" % "2.2.0" ,
  specs2 % Test
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
