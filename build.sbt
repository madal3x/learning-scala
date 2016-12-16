name := "scala-labs"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.ning" % "async-http-client" % "1.9.36",
  "org.jsoup" % "jsoup" % "1.8.3",
  "com.typesafe.akka" %% "akka-actor" % "2.4.3",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.3",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.3",
  "com.typesafe.akka" %% "akka-stream" % "2.4.4",
  "com.typesafe.akka" %% "akka-agent" % "2.4.4",
  "joda-time" % "joda-time" % "2.9.2",
  /*"io.reactivex" %% "rxscala" % "0.26.4",*/
  "com.netflix.rxjava" % "rxjava-scala" % "0.19.1",
  "org.lucee" % "commons-io" % "2.4.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.1.8"
)

val scalazVersion = "7.2.2"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % scalazVersion,
  "org.scalaz" %% "scalaz-effect" % scalazVersion,
  /*"org.scalaz" %% "scalaz-typelevel" % scalazVersion,*/
  "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test"
)

scalacOptions += "-feature"

initialCommands in console := "import scalaz._, Scalaz._"
