sbtPlugin := true

version := "0.0.5"

scalaVersion := "2.12.6"

updateOptions := updateOptions.value.withCachedResolution(true)

concurrentRestrictions in Global += Tags.limit(Tags.Test, 2)

useGpg := true

credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")

publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

libraryDependencies ++= Seq("com.typesafe" % "config" % "1.3.3")
