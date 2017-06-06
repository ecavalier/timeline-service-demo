logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.15")

// for autoplugins
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-M9")