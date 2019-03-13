package io.github.knoldus

import com.typesafe.config.ConfigFactory
import sbt.Keys._
import sbt._
import scala.collection.JavaConversions._
import scala.sys.process._

object CodeSquad extends AutoPlugin {

  lazy val codesquad = inputKey[Unit]("Run to upload Report's in CodeSquad.")

  val route = "http://52.15.45.40:8080/add/reports"

  val runReportsAndGetRegistrationKey = taskKey[String]("generate all configured reports and return registration key.")

  def uploadReport(file: String, organizationName: String, projectName: String, moduleName: String, registrationKey: String): Unit = {
    if (new File(file).exists())
      Seq("curl", "-X", "PUT", "-F", "projectName=" + projectName, "-F", "registrationKey=" + registrationKey, "-F",
        "moduleName=" + moduleName, "-F", "organisation=" + organizationName, "-F",
        "file=@" + file, route).!
  }


  def rawUploadReportSettings(): Seq[sbt.Def.Setting[_]] =
    Seq(
      codesquad := {
        val projectValue: String = name.value
        val organizationValue: String = organizationName.value
        val moduleValue: String = moduleName.value
        val targetValue = target.value
        val scalaVersionValue = "scala-" + scalaVersion.value.substring(0, 4)
        val registrationKey = runReportsAndGetRegistrationKey.value

        val scalaStyleFile = targetValue + "/scalastyle-result.xml"
        uploadReport(scalaStyleFile, organizationValue, projectValue, moduleValue, registrationKey)

        val scapegoatFile = targetValue + s"/$scalaVersionValue}/scapegoat-report/scapegoat.xml"
        uploadReport(scapegoatFile, organizationValue, projectValue, moduleValue, registrationKey)


        val cpdFile = targetValue + s"/$scalaVersionValue}/cpd/cpd.xml"
        uploadReport(cpdFile, organizationValue, projectValue, moduleValue, registrationKey)


        val sCoverageFile = targetValue + s"/$scalaVersionValue/scoverage-report/scoverage.xml"
        uploadReport(sCoverageFile, organizationValue, projectValue, moduleValue, registrationKey)

        val loc = targetValue + s"/$moduleValue.log"
        uploadReport(loc, organizationValue, projectValue, moduleValue, registrationKey)

      })

  override def projectSettings: Seq[sbt.Def.Setting[_]] = rawUploadReportSettings()

  override def requires: Plugins = sbt.plugins.JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  override def buildSettings: Seq[Def.Setting[_]] = Seq(
    runReportsAndGetRegistrationKey := {
      val path = (baseDirectory in ThisBuild).value / ".codesquad.conf"
      if (path.exists()) {
        val config = ConfigFactory.parseFile(path)
        val codesquadReports = config.getStringList("codesquad.reports").toList

        if (codesquadReports.contains("coverageReport"))
          Seq("sbt", "clean", "coverage", "test", "coverageReport").!

        if (codesquadReports.contains("scalastyle"))
          Seq("sbt", "scalastyle").!

        if (codesquadReports.contains("scapegoat"))
          Seq("sbt", "scapegoat").!

        if (codesquadReports.contains("cpd"))
          Seq("sbt", "cpd").!

        if(codesquadReports.contains("loc"))
          Seq("./lineOfCode.sh").!

        config.getString("codesquad.registrationKey")
      } else {
        throw new Exception("Please add .codesquad.conf file in project's baseDirectory.")
      }
    }
  )
}
