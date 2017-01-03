name := "scala_http_template"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.12"
val akkaHttpVersion = "10.0.1"

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.5",
  "org.joda" % "joda-convert" % "1.7",
  "com.typesafe" % "config" % "1.3.0",
  "ch.megard" %% "akka-http-cors" % "0.1.8",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "org.scalatest"     %% "scalatest" % "3.0.1" % "test",
  "com.jayway.restassured" % "rest-assured" % "2.9.0" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "ch.megard" %% "akka-http-cors" % "0.1.8",
  "net.logstash.logback" % "logstash-logback-encoder" % "4.5.1"

)
