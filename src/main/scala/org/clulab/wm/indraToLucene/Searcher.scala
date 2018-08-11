package org.clulab.wm.indraToLucene

import java.nio.file.Paths

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{IndexSearcher, ScoreDoc, TopScoreDocCollector}
import org.apache.lucene.store.FSDirectory

case class SearchResult(docId: Int, score: Float, textName: String, indraName: String, text: String, indra: String)

object Searcher {
  val MAX_HITS = 1000
}

class Searcher(indexDir: String) {
  val reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)))
  val searcher = new IndexSearcher(reader)

  def close() = reader.close()

  def search(queryString: String, maxHits: Int = Searcher.MAX_HITS): Array[SearchResult] =
      searchByField(queryString, maxHits, Indexer.TEXT_FIELD, new StandardAnalyzer())

  protected def searchByField(queryString: String, maxHits: Int, field: String, analyzer: Analyzer): Array[SearchResult] = {
    val query = new QueryParser(field, analyzer).parse(queryString)
    val collector = TopScoreDocCollector.create(maxHits)
    searcher.search(query, collector)
    val hits: Array[ScoreDoc] = collector.topDocs().scoreDocs
    var results = hits.map { hit =>
      val docId: Int = hit.doc
      val score: Float = hit.score
      val document = searcher.doc(docId)
      val textName = document.get(Indexer.TEXT_NAME_FIELD)
      val indraName = document.get(Indexer.INDRA_NAME_FIELD)
      val text = document.get(Indexer.TEXT_FIELD)
      val indra = document.get(Indexer.INDRA_FIELD)

      SearchResult(docId, score, textName, indraName, text, indra)
    }
    results
  }
}
