name := "indra-to-lucene"

version := "0.1"

scalaVersion := "2.12.4"

mainClass in Compile := Some("org.clulab.wm.indraToLucene.apps.SearcherApp")

libraryDependencies ++= {
  val ver = "5.3.1"

  Seq(
    "org.apache.lucene" %  "lucene-core"             % ver,
    "org.apache.lucene" %  "lucene-analyzers-common" % ver,
    "org.apache.lucene" %  "lucene-queryparser"      % ver,

    "org.json4s"        %% "json4s-native"           % "3.6.1",
    "org.json4s"        %% "json4s-jackson"          % "3.6.1",

    "org.scalatest"     %% "scalatest"               % "3.0.4" % "test"
  )
}
