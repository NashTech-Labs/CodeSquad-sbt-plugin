sbtPlugin := true

crossSbtVersions := Vector("0.13.11", "1.2.8")

crossScalaVersions := Vector("2.11.7","2.12.8")

scalacOptions := Seq("-deprecation", "-unchecked")

ThisBuild / organization := "io.github.knoldus"

name := "codesquad-sbt-plugin"

publishMavenStyle := true

version := "0.0.2"

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")

ThisBuild / organizationHomepage := Some(url("https://www.knoldus.com/home.knol"))

ThisBuild / libraryDependencies ++= Seq("com.typesafe" % "config" % "1.3.3", "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1")

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/knoldus/CodeSquad-sbt-plugin"),
    "scm:git@github.com:knoldus/CodeSquad-sbt-plugin.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "randhir1910",
    name  = "Randhir Kumar",
    email = "randhir.kumar@knoldus.in",
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
