name := "VictoriaAutomation"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1",
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.2",
  "ch.qos.logback" % "logback-classic" % "1.1.8",
  "org.scalatest" % "scalatest_2.11" % "3.2.0-SNAP1" % "test"
)




