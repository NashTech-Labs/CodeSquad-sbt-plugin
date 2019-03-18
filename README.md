# CodeSquad-sbt-plugin
A CodeSquad sbt plugin to automatic upload code quality report on CodeSquad server.

Step 1: In plugins.sbt

addSbtPlugin("io.github.knoldus" %% "codesquad-sbt-plugin" % "0.0.4")

Step 2: export registrationKey=......

Step 3: Add .codesquad.conf file in project directory

Step 4:- In .codesquad.conf file

codesquad
  {

reports = ["scalastyle","coverageReport","scapegoat","cpd","loc"] // Report's name which you want to upload in codesquad.For loc file name should be lineOfCode.sh

organisationName = "...."

projectName = "....."

}

Step 5:- sbt codesquad  // Run to upload reports in codesquad.
