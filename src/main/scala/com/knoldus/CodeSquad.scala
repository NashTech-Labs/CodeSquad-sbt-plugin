package com.knoldus

import sbt.Keys._
import sbt._
import scala.sys.process._
import scala.util.{Failure, Success, Try}

object CodeSquad extends AutoPlugin {

  val uploadReport = taskKey[Unit]("upload all report")
  val route = "http://52.15.45.40:8080/add/reports"
  val registrationKey: String = sys.env("registrationKey")

  private def uploadReport(file: String, organizationName: String, projectName: String, moduleName: String): Unit = {
    if (new File(file).exists())
      Seq("curl", "-X", "PUT", "-F", "projectName=" + projectName, "-F", "registrationKey=" + registrationKey, "-F",
        "moduleName=" + moduleName, "-F", "organisation=" + organizationName, "-F",
        "file=@" + file, route).!
  }

  lazy val baseCodeSquadPluginSettings: Seq[sbt.Def.Setting[_]] = Seq(
    uploadReport := {
      val projectValue: String = name.value
      val organizationValue: String = organizationName.value
      val moduleValue: String = moduleName.value
      val targetValue = target.value
      val scalaVersion1 = "scala-" + scalaVersion.value.substring(0, 4)

      Seq("sbt", "clean coverage test coverageReport")
      val sCoverageFile = targetValue + s"/$scalaVersion1/scoverage-report/scoverage.xml"
      uploadReport(sCoverageFile, organizationValue, projectValue, moduleValue)

      Try {
        Seq("sbt", "scalastyle").!
        val scalaStyleFile = targetValue + "/scalastyle-result.xml"
        uploadReport(scalaStyleFile, organizationValue, projectValue, moduleValue)
      } match {
        case Success(value) => println(value)
        case Failure(_) => println("Please add scalaStyle plugin")
      }

      Try {
        Seq("sbt", "scapegoat")
        val scapegoatFile = targetValue + s"/$scalaVersion1}/scapegoat-report/scapegoat.xml"
        uploadReport(scapegoatFile, organizationValue, projectValue, moduleValue)
      } match {
        case Success(value) => println(value)
        case Failure(_) => println("Please add scapegoat plugin")
      }

      Try {
        Seq("sbt", "cpd")
        val cpdFile = targetValue + s"/$scalaVersion1}/cpd/cpd.xml"
        uploadReport(cpdFile, organizationValue, projectValue, moduleValue)

      } match {
        case Success(value) => println(value)
        case Failure(_) => println("Please add cpd plugin")
      }

    })

  lazy val codeSquadPluginSettings: Seq[sbt.Def.Setting[_]] = baseCodeSquadPluginSettings


}