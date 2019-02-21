package com.knoldus

import sbt._
import Keys._

object CodeSquad extends AutoPlugin {

  val organisationName = taskKey[String]("organisation name")
  val projectName = taskKey[String]("project name")
  val moduleName1 = taskKey[String]("module name")
  val sampleConsole = taskKey[Unit]("console user input value")

  lazy val baseMyPluginSettings: Seq[sbt.Def.Setting[_]] = Seq(
    sampleConsole := {
     println(s"organisationName >> ${organisationName.value}")
     println(s"projectName >> ${projectName.value}")
     println(s"moduleName >> ${moduleName1.value}")
    }
  )
  lazy val myPluginSettings: Seq[sbt.Def.Setting[_]] = baseMyPluginSettings
}