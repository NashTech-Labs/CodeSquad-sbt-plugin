package io.github.knoldus

import java.io.FileNotFoundException
import com.typesafe.config.ConfigFactory
import sbt.Keys._
import sbt._
import scala.collection.JavaConversions._

object CodeSquad extends AutoPlugin {

  lazy val codesquad = inputKey[Unit]("Run to upload Report's in CodeSquad.")

  val route = "http://52.15.45.40:8080/add/reports"

  val runReport = taskKey[(String, String, String, String)]("generate all configured reports.")

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
        val (scalaV, organizationName, projectName, registrationKey) = runReport.value

        val sCoverageFile = targetValue + s"/$scalaV/scoverage-report/scoverage.xml"
        uploadReport(sCoverageFile, organizationName, projectName, module, registrationKey)

        val scalaStyleFile = targetValue + "/scalastyle-result.xml"
        uploadReport(scalaStyleFile, organizationName, projectName, module, registrationKey)

        val scapegoatFile = targetValue + s"/$scalaV/scapegoat-report/scapegoat.xml"
        uploadReport(scapegoatFile, organizationName, projectName, module, registrationKey)

        val cpdFile = targetValue + s"/$scalaV/cpd/cpd.xml"
        uploadReport(cpdFile, organizationName, projectName, module, registrationKey)

        val loc = targetValue + s"/$module.log"
        uploadReport(loc, organizationName, projectName, module, registrationKey)

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

        val scalaV = s"scala-${scalaVersion.value.substring(0,4)}"

        Seq("sbt","clean").!

        if (reportsName.contains("coverageReport")) Seq("sbt", "coverage", "test", "coverageReport").!

        if (reportsName.contains("scalastyle")) Seq("sbt", "scalastyle").!

        if (reportsName.contains("scapegoat")) Seq("sbt", "scapegoat").!

        if (reportsName.contains("cpd")) Seq("sbt", "cpd").!

        if (reportsName.contains("loc")) Seq("bash","loc.sh").!

        (scalaV, organisationName, projectName, registrationKey)

      } else {
        throw new FileNotFoundException("Please add .codesquad.conf file in project's baseDirectory.")
      }
    }
  )
}