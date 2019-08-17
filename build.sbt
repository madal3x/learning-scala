name := "scala-labs"

version := "1.0"

scalaVersion := "2.11.12"


libraryDependencies ++= Seq(
  "com.ning" % "async-http-client" % "1.9.36",
  "org.jsoup" % "jsoup" % "1.8.3",
  "com.typesafe.akka" %% "akka-actor" % "2.5.2",
  "com.typesafe.akka" %% "akka-persistence" % "2.5.2",
  "org.scalaz" %% "scalaz-core" % "7.2.8",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.2",
  "com.typesafe.akka" %% "akka-stream" % "2.5.2",
  "com.typesafe.akka" %% "akka-agent" % "2.5.2",
  "joda-time" % "joda-time" % "2.9.2",
  /*"io.reactivex" %% "rxscala" % "0.26.4",*/
  "com.netflix.rxjava" % "rxjava-scala" % "0.19.1",
  "org.lucee" % "commons-io" % "2.4.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.1.8",
  "io.verizon.delorean" %% "core" % "1.2.40-scalaz-7.2",
  "com.entopix" % "maui" % "1.3.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.7.0",
  "info.bliki.wiki" % "bliki" % "3.1.0",
  "org.apache.logging.log4j" % "log4j" % "2.8",
  "com.chuusai" %% "shapeless" % "2.3.3",
  //"org.typelevel" %% "cats" % "0.9.0",
  "org.typelevel" %% "cats-core" % "1.1.0",
  "org.typelevel" %% "cats-free" % "1.1.0",
  "com.codecommit" %% "emm-core" % "0.2.1",
  "com.codecommit" %% "emm-cats" % "0.2.1",
  "org.spire-math" %% "spire" % "0.13.0",
  "org.apache.poi" % "poi-ooxml" % "3.15"
)

val scalazVersion = "7.2.8"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % scalazVersion,
  "org.scalaz" %% "scalaz-effect" % scalazVersion,
  /*"org.scalaz" %% "scalaz-typelevel" % scalazVersion,*/
  "org.scalaz" %% "scalaz-concurrent" % scalazVersion,
  "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test"
)

scalacOptions += "-feature"

initialCommands in console := "import scalaz._, Scalaz._"

addCommandAlias("make-idea", ";update-classifiers; update-sbt-classifiers; gen-idea sbt-classifiers")