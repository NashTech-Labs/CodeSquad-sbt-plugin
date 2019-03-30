package io.github.knoldus

import java.io.FileNotFoundException
import com.typesafe.config.ConfigFactory
import sbt.Keys._
import sbt._
import scala.collection.JavaConversions._

object CodeSquad extends AutoPlugin {

  lazy val codesquad = inputKey[Unit]("Run to upload Report's in CodeSquad.")

  val route = "http://52.15.45.40:8080/add/reports"

  val runReport = settingKey[(List[String], String, String, String)]("generate all configured reports.")

  def uploadReport(file: String, organizationName: String, projectName: String, moduleName: String, registrationKey: String): Unit = {
    if (new File(file).exists())
      Seq("curl", "-X", "PUT", "-F", "projectName=" + projectName, "-F", "registrationKey=" + registrationKey, "-F",
        "moduleName=" + moduleName, "-F", "organisation=" + organizationName, "-F",
        "file=@" + file, route).!
  }


  def rawUploadReportSettings(): Seq[sbt.Def.Setting[_]] =
    Seq(
      codesquad := {
        val module: String = moduleName.value
        val targetValue = (baseDirectory in ThisBuild).value +s"/$module/target"
        val scalaVersion1 = s"scala-${scalaVersion.value.substring(0,4)}"

        val (reportsName, organizationName, projectName, registrationKey) = runReport.value

        if (reportsName.contains("coverageReport")) {
          Seq("sbt", "clean", "coverage", "test", "coverageReport").!
          val sCoverageFile = targetValue + s"/$scalaVersion1/scoverage-report/scoverage.xml"
          uploadReport(sCoverageFile, organizationName, projectName, module, registrationKey)
        }

        if (reportsName.contains("scalastyle")) {
          Seq("sbt", "scalastyle").!
          val scalaStyleFile = targetValue + "/scalastyle-result.xml"
          uploadReport(scalaStyleFile, organizationName, projectName, module, registrationKey)
        }

        if (reportsName.contains("scapegoat")) {
          Seq("sbt", "scapegoat").!
          val scapegoatFile = targetValue + s"/$scalaVersion1/scapegoat-report/scapegoat.xml"
          uploadReport(scapegoatFile, organizationName, projectName, module, registrationKey)
        }

        if (reportsName.contains("cpd")) {
          Seq("sbt", "cpd").!
          val cpdFile = targetValue + s"/$scalaVersion1/cpd/cpd.xml"
          uploadReport(cpdFile, organizationName, projectName, module, registrationKey)
        }

        if (reportsName.contains("lineOfCode")) {
          Seq("./lineOfCode.sh").!
          val loc = targetValue + s"/$module.log"
          uploadReport(loc, organizationName, projectName, module, registrationKey)
        }

        if (reportsName.contains("loc")) {
          val loc = targetValue + "/stats.log"
          uploadReport(loc, organizationName, projectName, module, registrationKey)
        }


      })

  override def projectSettings: Seq[sbt.Def.Setting[_]] = rawUploadReportSettings()

  override def requires: Plugins = sbt.plugins.JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  override def buildSettings: Seq[Def.Setting[_]] = Seq(
    runReport := {
      val path = (baseDirectory in ThisBuild).value / ".codesquad.conf"

      if (path.exists()) {
        val config = ConfigFactory.parseFile(path)
        val reportsName = config.getStringList("codesquad.reports").toList
        val organisationName = config.getString("codesquad.organisationName")
        val projectName = config.getString("codesquad.projectName")
        val registrationKey = sys.env("registrationKey")

        if (registrationKey == null)
          throw new IllegalArgumentException("Please set registrationKey in environment variable.")
        if (reportsName.isEmpty)
          throw new NullPointerException("Please add reports name in .codesquad.conf file.")
        if (organisationName == null || projectName == null)
          throw new NullPointerException("Please add organisationName and projectName in .codesquad.conf file.")

        (reportsName, organisationName, projectName, registrationKey)

      } else {
        throw new FileNotFoundException("Please add .codesquad.conf file in project's baseDirectory.")
      }
    }
  )
}
