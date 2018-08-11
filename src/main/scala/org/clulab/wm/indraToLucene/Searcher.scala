package org.clulab.wm.indraToLucene

import java.nio.file.Paths

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{IndexSearcher, ScoreDoc, TopScoreDocCollector}
import org.apache.lucene.store.FSDirectory

case class SearchResult(docId: Int, score: Float, text: String, indra: String)

class Searcher(indexDir: String) {
  val reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)))
  val searcher = new IndexSearcher(reader)

  def close() = reader.close()

  def search(query: String, totalHits: Int = Searcher.TOTAL_HITS): Seq[SearchResult] =
      searchByField(query, Indexer.TEXT_FIELD, new StandardAnalyzer(), totalHits)

  protected def searchByField(queryString: String, field: String, analyzer: Analyzer, totalHits: Int, verbose: Boolean = true): Array[SearchResult] = {
    val query = new QueryParser(field, analyzer).parse(queryString)
    val collector = TopScoreDocCollector.create(totalHits)
    searcher.search(query, collector)
    val hits: Array[ScoreDoc] = collector.topDocs().scoreDocs
    var results = hits.map { hit =>
      val docId: Int = hit.doc
      val score: Float = hit.score
      val document = searcher.doc(docId)
      val text = document.get(Indexer.TEXT_FIELD)
      val indra = document.get(Indexer.INDRA_FIELD)

      SearchResult(docId, score, text, indra)
    }
    results
  }
}

object Searcher {
  val TOTAL_HITS = 500000
}