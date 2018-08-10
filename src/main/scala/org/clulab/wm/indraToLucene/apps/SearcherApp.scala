package org.clulab.wm.indraToLucene.apps

import org.clulab.wm.indraToLucene.Searcher

object SearcherApp extends App {
  var indexDir = "D:/Users/kwa/Documents/MyData/Projects/indra-to-lucene/indra-to-lucene-clone/indexDir"
  val searcher = new Searcher(indexDir)

  val hunger = searcher.search("hunger")
  val famine = searcher.search("famine")
}
