package io.github.knoldus

import com.typesafe.config.{Config, ConfigFactory}
import sbt.Keys._
import sbt._
import collection.JavaConversions._
import scala.sys.process._

object CodeSquad extends AutoPlugin {

  lazy val codesquad = inputKey[Unit]("Run uploadReport on your code")

  val route = "http://18.221.78.85:8080/add/reports"
  val config: Config = ConfigFactory.load("codesquad.conf")
  val codesquadReports: List[String] = config.getStringList("codesquad.reports").toList
  val registrationKey: String = config.getString("codesquad.registrationKey")

  def uploadReport(file: String, organizationName: String, projectName: String, moduleName: String): Unit = {
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
        val scalaVersion1 = "scala-" + scalaVersion.value.substring(0, 4)

        if (codesquadReports.contains("coverageReport"))
          Seq(s"sbt", "clean coverage test coverageReport").!


        if (codesquadReports.contains("scalastyle"))
          Seq("sbt",""""project core"""", "scalastyle").!


        if (codesquadReports.contains("scapegoat"))
          Seq(s"sbt", "scapegoat").!

        if (codesquadReports.contains("cpd"))
          Seq(s"sbt", "cpd").!


        val scalaStyleFile = targetValue + "/scalastyle-result.xml"
        uploadReport(scalaStyleFile, organizationValue, projectValue, moduleValue)

        val scapegoatFile = targetValue + s"/$scalaVersion1}/scapegoat-report/scapegoat.xml"
        uploadReport(scapegoatFile, organizationValue, projectValue, moduleValue)


        val cpdFile = targetValue + s"/$scalaVersion1}/cpd/cpd.xml"
        uploadReport(cpdFile, organizationValue, projectValue, moduleValue)


        val sCoverageFile = targetValue + s"/$scalaVersion1/scoverage-report/scoverage.xml"
        uploadReport(sCoverageFile, organizationValue, projectValue, moduleValue)


      })

  override def projectSettings: Seq[sbt.Def.Setting[_]] = rawUploadReportSettings()

  override def requires: Plugins = sbt.plugins.JvmPlugin

  override def trigger: PluginTrigger = allRequirements

}
