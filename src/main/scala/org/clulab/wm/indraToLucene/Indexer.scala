package org.clulab.wm.indraToLucene

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, StoredField, TextField}
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory

import java.nio.file.Paths

class Indexer {

  def index(textDir: String, indraDir: String, indexDir: String, fileMapping: Utils.FileMapping): Unit = {
    val analyzer = new StandardAnalyzer
    val config = new IndexWriterConfig(analyzer)
    val index = FSDirectory.open(Paths.get(indexDir))
    val writer = new IndexWriter(index, config)
    val textFiles = Utils.findFiles(textDir, "txt")

    for (textFile <- textFiles) {
      val indraFile = fileMapping(textFile, indraDir)

      if (indraFile.exists()) {
        println(indraFile.getName())
        val text = Utils.getContents(textFile)
        val indra = Utils.getContents(indraFile)

        addDocument(writer, text, indra)
      }
    }
    writer.close()
  }

  protected def addDocument(writer: IndexWriter, text: String, indra: String): Unit = {
    val document = new Document

    document.add(new TextField(Indexer.TEXT_FIELD, text, Field.Store.YES))
    document.add(new StoredField(Indexer.INDRA_FIELD, indra))
    writer.addDocument(document)
  }
}

object Indexer {
  val INDEX_DIR = "D:/Users/kwa/Documents/MyData/Projects/indra-to-lucene/indra-to-lucene-clone/indexDir"

  val TEXT_FIELD = "text"
  val INDRA_FIELD = "indra"
}