import com.typesafe.sbt.pgp.PgpKeys.{publishLocalSigned, publishSigned, useGpg}

sbtPlugin := true

ThisBuild / organization := "io.github.knoldus"
ThisBuild / organizationName := "Knoldus"
ThisBuild / organizationHomepage := Some(url("http://www.knoldus.com/"))

sonatypeProfileName := "io.github.knoldus"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/knoldus/CodeSquad-sbt-plugin"),
    "scm:git@github.com:knoldus/CodeSquad-sbt-plugin.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "narayankumar",
    name  = "Narayan Kumar",
    email = "narayan.@knoldus.com",
    url   = url("https://github.com/knoldus/CodeSquad-sbt-plugin")
  )
)


ThisBuild / description := "It will upload reports in codesquad."
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/knoldus/CodeSquad-sbt-plugin"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)