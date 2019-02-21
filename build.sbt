lazy val root = (project in file("."))
  .settings(
    name := "codesquad",
    organization := "com.knoldus",
    version := "0.1",
    scalaVersion := "2.12.8",
    sbtPlugin := true,
    scriptedBufferLog := false
  )