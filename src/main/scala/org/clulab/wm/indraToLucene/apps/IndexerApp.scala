package org.clulab.wm.indraToLucene.apps

import java.io.File

import org.clulab.wm.indraToLucene.Indexer

object IndexerApp extends App {
  val TEXT_DIR = "./text"
  val INDRA_DIR = "./indra"

  val MAPPING_52 = "52"
  val MAPPING_17K = "17k"

  protected def mapping52(textFile: File, indraDir: String): File = {
    val name = textFile.getName
    val prefix = name.slice(0, name.indexOf('_'))

    new File(indraDir + "/" + prefix + "_success.json")
  }

  protected def mapping17k(textFile: File, indraDir: String): File = {
    val name = textFile.getName
    val prefix = name.slice(0, name.lastIndexOf('.'))
    val base = indraDir + "/" + prefix
    val successFile = new File(base + "_success.json")
    val failedFile = new File(base + "_failed.json")

    if (successFile.exists()) successFile
    else failedFile
  }

  if (args.size != 4 || (args.size >= 4 && args(3) != "52" && args(3) != "17k")) {
    println("Syntax: " + this.getClass().getName() + " textDir indraDir indexDir " + MAPPING_52 + "|" + MAPPING_17K)
    System.exit(-1)
  }

  val textDir =
      if (args.size >= 1) args(0)
      else TEXT_DIR
  val indraDir =
      if (args.size >= 2) args(1)
      else INDRA_DIR
  val indexDir =
      if (args.size >= 3) args(2)
      else Indexer.INDEX_DIR
  val mapper =
      if (args.size >= 4) args(3)
      else MAPPING_17K
  val fileMapping =
      if (mapper == MAPPING_52) mapping52 _
      else mapping17k _

  new Indexer().index(textDir, indraDir, indexDir, fileMapping)
}
