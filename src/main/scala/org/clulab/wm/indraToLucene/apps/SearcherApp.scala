package org.clulab.wm.indraToLucene.apps

import org.clulab.wm.indraToLucene.Indexer
import org.clulab.wm.indraToLucene.Searcher

object SearcherApp extends App {
  val MAX_HITS = 1000

  if (args.size < 2 || 3 < args.size) {
    println("Syntax: " + this.getClass().getName() + " indexDir \"query\" [maxHits]")
    System.exit(-1)
  }

  var indexDir =
      if (args.size > 0) args(0)
      else Indexer.INDEX_DIR
  var queryString =
    if (args.size > 1) args(1)
    else "famine"
  var maxHits =
      if (args.size > 2) args(2).toInt
      else MAX_HITS

  val searcher = new Searcher(indexDir)
  val results = searcher.search(queryString, maxHits)
  searcher.close()

  def linearize(text: String) = text
      .replace('\t', ' ')
      .replace('\n', ' ')

  results.foreach { result =>
    println("" + result.score +
        "\t" + linearize(result.textName) +
        "\t" + linearize(result.indraName) +
        "\t" + linearize(result.text) +
        "\t" + linearize(result.indra)
    )
  }
}
