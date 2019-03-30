sbtPlugin := true

organization := "io.github.knoldus"

name := "codesquad-sbt-plugin"

publishMavenStyle := true


version := "0.1.0"

scalaVersion := "2.10.4"

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")

organizationHomepage := Some(url("https://www.knoldus.com/home.knol"))

useGpg := true

libraryDependencies ++= Seq("com.typesafe" % "config" % "1.3.2")

 scmInfo := Some(
  ScmInfo(
    url("https://github.com/knoldus/CodeSquad-sbt-plugin"),
    "scm:git@github.com:knoldus/CodeSquad-sbt-plugin.git"
  )
)
developers := List(
  Developer(
    id    = "randhir1910",
    name  = "Randhir Kumar",
    email = "randhir.kumar@knoldus.in",
    url   = url("https://github.com/knoldus/CodeSquad-sbt-plugin")
  )
)

description := "It will upload reports in codesquad."
licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://github.com/knoldus/CodeSquad-sbt-plugin"))

// Remove all additional repository other than Maven Central from POM
 pomIncludeRepository := { _ => false }
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
