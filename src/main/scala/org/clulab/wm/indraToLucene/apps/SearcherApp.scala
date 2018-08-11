package org.clulab.wm.indraToLucene.apps

import org.clulab.wm.indraToLucene.Indexer
import org.clulab.wm.indraToLucene.Searcher

object SearcherApp extends App {
//  if (args.size != 2) {
//    println("Syntax: " + this.getClass().getName() + " indexDir query")
//    System.exit(-1)
//  }

  var indexDir =
      if (args.size >= 1) args(0)
      else Indexer.INDEX_DIR
  var queryString =
      if (args.size >= 2) args(1)
      else "famine"

  val searcher = new Searcher(indexDir)
  val results = searcher.search(queryString)
  searcher.close()

  results.foreach { result =>
    println("" + result.score + "\t" + result.text.replace('\t', ' ').replace('\n', ' '))
  }
}
