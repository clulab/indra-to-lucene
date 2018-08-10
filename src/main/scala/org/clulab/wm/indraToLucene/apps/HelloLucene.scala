package org.clulab.wm.indraToLucene.apps

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, StoredField, StringField, TextField}
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.TopScoreDocCollector

class HelloLucene {
  val hitsPerPage = 10

  val (analyzer, ramDirectory) = index()

  protected def index(): (StandardAnalyzer, RAMDirectory) = {

    def addDoc(indexWriter: IndexWriter, title: String, isbn: String) = {
      val document = new Document()

      document.add(new TextField("title", title, Field.Store.YES))
      document.add(new StringField("isbn", isbn, Field.Store.YES))
      indexWriter.addDocument(document)
    }

    val analyzer = new StandardAnalyzer()
    val index = new RAMDirectory()
    val config = new IndexWriterConfig(analyzer)
    val indexWriter = new IndexWriter(index, config)

    addDoc(indexWriter, "Lucene in Action", "193398817")
    addDoc(indexWriter, "Lucene for Dummies", "55320055Z")
    addDoc(indexWriter, "Managing Gigabytes", "55063554A")
    addDoc(indexWriter, "The Art of Computer Science", "9900333X")
    indexWriter.close()
    (analyzer, index)
  }

  def query(queryString: String) = {
    val query = new QueryParser("title", analyzer).parse(queryString)
    val reader = DirectoryReader.open(ramDirectory)
    val searcher = new IndexSearcher(reader)
    val collector = TopScoreDocCollector.create(hitsPerPage) // , true)

    searcher.search(query, collector)
    val hits = collector.topDocs().scoreDocs

    println("Found " + hits.length + " hits.")
    for (i <- 0 until hits.size) {
      val docId = hits(i).doc
      val document = searcher.doc(docId)
      println((i + 1) + ". " + document.get("isbn") + "\t" + document.get("title"))
    }
    reader.close()
  }
}

object HelloLuceneApp extends App {
  val sample = new HelloLucene()

  sample.query("Lucene")
  sample.query("Art")
}