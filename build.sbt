name := "scala-labs"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.ning" % "async-http-client" % "1.9.36",
  "org.jsoup" % "jsoup" % "1.8.3",
  "com.typesafe.akka" %% "akka-actor" % "2.4.3",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.3",
  "org.scalaz" %% "scalaz-core" % "7.2.2",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.3"
)