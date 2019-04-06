sbtPlugin := true

version := "0.0.6"

scalaVersion := "2.12.8"

updateOptions := updateOptions.value.withCachedResolution(true)

concurrentRestrictions in Global += Tags.limit(Tags.Test, 2)

useGpg := true

credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

libraryDependencies ++= Seq("com.typesafe" % "config" % "1.3.3")
