package com.nicta.scoobi.guide

import io.Source
import com.nicta.scoobi.impl.control.Exceptions._

object ScoobiVersion {

  lazy val version = versionLine.flatMap(extractVersion).getOrElse("version not found")

  lazy val branch = if (version endsWith "SNAPSHOT") "master" else version

  private lazy val versionLine = buildSbt.flatMap(_.getLines.find(line => line contains "version"))
  private def extractVersion(line: String) = "version\\s*\\:\\=\\s*\"(.*)\"".r.findFirstMatchIn(line).map(_.group(1))

  lazy val buildSbt =
    tryo(Source.fromFile("build.sbt"))((e:Exception) => println("can't find the build.sbt file "+e.getMessage))

  implicit def toVersionedText(t: String): VersionedText = VersionedText(t)
  case class VersionedText(t: String) {
    /**
     * set the version and branch tags in the pages
     */
    def setVersionAndBranch = {
      t.replaceAll("SCOOBI_VERSION", version)
      t.replaceAll("SCOOBI_BRANCH", branch)
    }
  }

}
