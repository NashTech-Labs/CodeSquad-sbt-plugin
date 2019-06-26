# CodeSquad-sbt-plugin for sbt 1.x. // (Scala 2.12)
A CodeSquad sbt plugin to automatic upload code quality report on CodeSquad server. In order to use this plugin follow the below steps.

Step 1: In plugins.sbt

addSbtPlugin("io.github.knoldus" %% "codesquad-sbt-plugin" % "0.2.1")

Step 2: export registrationKey=......

Step 3: Add .codesquad.conf file in project directory

Step 4:- In .codesquad.conf file

codesquad
  {

reports = ["scalastyle","coverageReport","scapegoat","cpd","loc"] // Report's name which you want to upload in codesquad. For loc report use plugin:   addSbtPlugin("io.github.knoldus" % "codesquad-loc-plugin" % "0.1.0")


organisationName = "...."

projectName = "....."

}

Step 5:- sbt codesquad  // Run to upload reports in codesquad.



# CodeSquad-sbt-plugin for sbt 0.13. // (Scala 2.10 and Scala 2.11)

A CodeSquad sbt plugin to automatic upload code quality report on CodeSquad server. In order to use this plugin follow the below steps.

Step 1: In plugins.sbt

addSbtPlugin("io.github.knoldus" %% "codesquad-sbt-plugin" % "0.1.8")

Step 2: export registrationKey=......

Step 3: Add .codesquad.conf file in project directory

Step 4:- In .codesquad.conf file

codesquad {

reports = ["scalastyle","coverageReport","scapegoat","cpd","loc"] // Report's name which you want to upload in codesquad. For loc report use plugin: addSbtPlugin("io.github.knoldus" % "codesquad-loc-plugin" % "0.0.1")


organisationName = "...."

projectName = "....."

}

Step 5:- sbt codesquad // Run to upload reports in codesquad.
