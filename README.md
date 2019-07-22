# Prerequisite for CodeSquad plugin:
1. Please add all required plugins dependencies in plugins.sbt file and plugins version should be compatible with your project Scala version.

Example: If we want to upload ["scalastyle","coverageReport","scapegoat","cpd" and "loc"] quality reports in CodeSquad.
For Scala 2.12.X plugins.sbt file should look like this.

- addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
- addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.0")
- addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.3.5")
- addSbtPlugin("com.github.sbt" % "sbt-cpd" % "2.0.0")
- addSbtPlugin("io.github.knoldus" %% "codesquad-sbt-plugin" % "0.2.1")
- addSbtPlugin("io.github.knoldus" % "codesquad-loc-plugin" % "0.1.0")


2. To get registrationKey https://www.getcodesquad.com/dashboard/help.

# CodeSquad-sbt-plugin for sbt 1.x. // (Scala 2.12) 
#### [Maven Central Repository](https://search.maven.org/artifact/io.github.knoldus/codesquad-sbt-plugin/0.2.1/jar)

A CodeSquad sbt plugin to automatic upload code quality report on CodeSquad server. In order to use this plugin follow the below steps.

Step 1: In plugins.sbt

addSbtPlugin("io.github.knoldus" %% "codesquad-sbt-plugin" % "0.2.1")

Step 2: export registrationKey=......

Step 3: Add .codesquad.conf file in project directory

Step 4:- In .codesquad.conf file

codesquad
  {

reports = ["scalastyle","coverageReport","scapegoat","cpd","loc"]

organisationName = "...."

projectName = "....."

}

Step 5:- sbt codesquad  // Run to upload reports in codesquad.



# CodeSquad-sbt-plugin for sbt 0.13. // (Scala 2.10 and Scala 2.11)
#### [Maven Central Repository](https://search.maven.org/artifact/io.github.knoldus/codesquad-sbt-plugin/0.1.8/jar)

A CodeSquad sbt plugin to automatic upload code quality report on CodeSquad server. In order to use this plugin follow the below steps.

Step 1: In plugins.sbt

addSbtPlugin("io.github.knoldus" %% "codesquad-sbt-plugin" % "0.1.8")

Step 2: export registrationKey=......

Step 3: Add .codesquad.conf file in project directory

Step 4:- In .codesquad.conf file

codesquad {

reports = ["scalastyle","coverageReport","scapegoat","cpd","loc"]

organisationName = "...."

projectName = "....."

}

Step 5:- sbt codesquad // Run to upload reports in codesquad.
