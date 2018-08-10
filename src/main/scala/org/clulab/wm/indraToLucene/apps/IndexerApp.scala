package org.clulab.wm.indraToLucene.apps

import org.clulab.wm.indraToLucene.Indexer

object IndexerApp extends App {
  var textDir = "/D:/Users/kwa/Documents/MyData/Projects/indra-to-lucene/indra-to-lucene-clone/DocAll"
  var indraDir = "/D:/Users/kwa/Documents/MyData/Projects/indra-to-lucene/indra-to-lucene-clone/tFTD_stmts/tFTD_stmts"

  var indexDir = "D:/Users/kwa/Documents/MyData/Projects/indra-to-lucene/indra-to-lucene-clone/indexDir"

  new Indexer().index(textDir, indraDir, indexDir)
}
