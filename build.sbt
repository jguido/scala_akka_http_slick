name := "scala_http_template"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

val akkaVersion = "2.4.12"
val akkaHttpVersion = "10.0.1"
val akkaDeps = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion
)

val slickDeps = Seq(
  "com.typesafe.slick" %% "slick" % "3.0.0"
)

val testDeps = Seq(

  "org.scalatest"     %% "scalatest" % "3.0.1" % "test",
  "com.jayway.restassured" % "rest-assured" % "2.9.0" % "test",
  "com.h2database" % "h2" % "1.3.175"
)

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.5",
  "org.joda" % "joda-convert" % "1.7",
  "com.typesafe" % "config" % "1.3.0",
  "ch.megard" %% "akka-http-cors" % "0.1.8",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "net.logstash.logback" % "logstash-logback-encoder" % "4.5.1",
  "org.flywaydb" % "flyway-core" % "3.1",
  "com.jason-goodwin" %% "authentikat-jwt" % "0.4.1",
  "org.scala-lang" % "scala-reflect" % "2.11.8"

) ++ akkaDeps ++ slickDeps ++ testDeps

