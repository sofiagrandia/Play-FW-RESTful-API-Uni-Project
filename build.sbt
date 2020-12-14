name := """proyectosI"""
organization := "com.sof"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.3"

libraryDependencies += guice
libraryDependencies ++= Seq(
  javaJdbc
)
/*
libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.11.908"
)

javaOptions ++= Seq(
  "-Djavax.net.ssl.trustStore=project/cassandra_truststore.jks",
  "-Djavax.net.ssl.trustStorePassword=amazon"
)

// Must enable JVM forking to use javaOptions with runAll.
fork := true*/
