organization := "com.evojam"

name := "mongo-drivers-performance"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.11.7")

scalacOptions ++= Seq(
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

resolvers ++= Seq(
  "Maven Central Mirror UK" at "http://uk.maven.org/maven2",
  Resolver.sbtPluginRepo("snapshots"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases"),
  Resolver.typesafeRepo("releases")
)

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.storm-enroute" %% "scalameter" % "0.6",
  "org.reactivemongo" %% "reactivemongo" % "0.11.2",
  "com.evojam" %% "mongo-driver-scala" % "0.4.4-SNAPSHOT",
  "org.mongodb" % "mongodb-driver-async" % "3.0.2"
)
