name := "VictoriaAutomation"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1", //cread config file
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.2", // java-cassandra driver
  "ch.qos.logback" % "logback-classic" % "1.1.8", //logging
  "org.scalatest" % "scalatest_2.11" % "3.2.0-SNAP1" % "test", // for scalaTest testing framework
  "joda-time" % "joda-time" % "2.9.7" //for time parsing
)

assemblyMergeStrategy in assembly := {
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  // case "META-INF/io.netty.versions.properties" => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}





