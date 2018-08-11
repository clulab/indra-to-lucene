package org.clulab.wm.indraToLucene.apps

import java.io.File

import org.clulab.wm.indraToLucene.Indexer

object IndexerApp extends App {
//  val TEXT_DIR = "/D:/Users/kwa/Documents/MyData/Projects/indra-to-lucene/indra-to-lucene-clone/DocAll"
//  val INDRA_DIR = "/D:/Users/kwa/Documents/MyData/Projects/indra-to-lucene/indra-to-lucene-clone/tFTD_stmts/tFTD_stmts"

  val TEXT_DIR = "/D:/Users/kwa/Documents/MyData/Projects/indra-to-lucene/indra-to-lucene-clone/UN_text_content/UN_text_content"
  val INDRA_DIR = "/D:/Users/kwa/Documents/MyData/Projects/indra-to-lucene/indra-to-lucene-clone/UN_stmt_jsons_by_file/UN_stmt_jsons_by_file"

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

//  if (args.size != 4 || (args.size >= 4 && args(3) != "52" && args(3) != "17k")) {
//    println("Syntax: " + this.getClass().getName() + " textDir indraDir indexDir " + MAPPING_52 + "|" + MAPPING_17K)
//    System.exit(-1)
//  }

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
