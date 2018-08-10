package org.clulab.wm.indraToLucene

import java.nio.file.Paths
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.store.FSDirectory

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{TopScoreDocCollector, IndexSearcher}
import scala.collection.mutable

class Searcher(indexDir: String) {

  case class SearchResult(docId: Int, score: Float, text: String, indra: String)

  val TOTAL_HITS = 500000

  val reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)))
  val searcher = new IndexSearcher(reader)

  def close() = reader.close()

  def search(query: String, totalHits: Int = TOTAL_HITS): Seq[SearchResult] =
      searchByField(query, "text", new StandardAnalyzer(), totalHits)

  protected def searchByField(query: String, field: String, analyzer: Analyzer, totalHits: Int = TOTAL_HITS, verbose: Boolean = true): Seq[SearchResult] = {
    val q = new QueryParser(field, analyzer).parse(query)
    val collector = TopScoreDocCollector.create(totalHits)
    searcher.search(q, collector)
    val hits = collector.topDocs().scoreDocs
    var results: Seq[SearchResult] = Seq.empty

    for (hit <- hits) {
      val docId: Int = hit.doc
      val score: Float = hit.score
      val document = searcher.doc(docId)
      val text = document.get("text")
      val indra = document.get("indra")

      // Sort according to score?
      results = new SearchResult(docId, score, text, indra) +: results
    }
    results
  }
}
