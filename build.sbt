name := "indra-to-lucene"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= {
  val luceVer = "5.3.1"

  Seq(
    "org.apache.lucene" % "lucene-core"             % luceVer,
    "org.apache.lucene" % "lucene-analyzers-common" % luceVer,
    "org.apache.lucene" % "lucene-queryparser"      % luceVer
  )
}
