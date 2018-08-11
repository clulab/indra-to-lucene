package org.clulab.wm.indraToLucene

import java.io.File

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, StoredField, TextField}
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory
import java.nio.file.Paths


object Indexer {
  val INDEX_DIR = "./index"

  val TEXT_NAME_FIELD = "textName"
  val INDRA_NAME_FIELD = "indraName"

  val TEXT_FIELD = "text"
  val INDRA_FIELD = "indra"
}

class Indexer {

  def index(textDir: String, indraDir: String, indexDir: String, fileMapping: Utils.FileMapping): Unit = {
    val analyzer = new StandardAnalyzer
    val config = new IndexWriterConfig(analyzer)

    val indexFile = new File(indexDir)
    if (!indexFile.exists())
      indexFile.mkdirs()

    val index = FSDirectory.open(Paths.get(indexDir))
    val writer = new IndexWriter(index, config)
    val textFiles = Utils.findFiles(textDir, "txt")

    for (textFile <- textFiles) {
      val indraFile = fileMapping(textFile, indraDir)

      if (indraFile.exists()) {
        println(indraFile.getName())
        val text = Utils.getContents(textFile)
        val indra = Utils.getContents(indraFile)

        addDocument(writer, textFile.getName(), indraFile.getName(), text, indra)
      }
    }
    writer.close()
  }

  protected def addDocument(writer: IndexWriter, textName: String, indraName: String, text: String, indra: String): Unit = {
    val document = new Document

    document.add(new StoredField(Indexer.TEXT_NAME_FIELD, textName))
    document.add(new StoredField(Indexer.INDRA_NAME_FIELD, indraName))
    document.add(new TextField(Indexer.TEXT_FIELD, text, Field.Store.YES))
    document.add(new StoredField(Indexer.INDRA_FIELD, indra))
    writer.addDocument(document)
  }
}
