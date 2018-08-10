package org.clulab.wm.indraToLucene

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, StoredField, StringField, TextField}
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory

import java.io.File
import java.nio.file.Paths
import scala.io.Source

class Indexer {

  def index(textDir: String, indraDir: String, indexDir: String): Unit = {

    def getContents(file: File) = {
      val source = Source.fromFile(file)
      val rawText = source.getLines.mkString("\n")

      source.close()
      rawText
    }

    val analyzer = new StandardAnalyzer
    val config = new IndexWriterConfig(analyzer)
    val index = FSDirectory.open(Paths.get(indexDir))
    val writer = new IndexWriter(index, config)
    val textFiles = Files.findFiles(textDir, "txt")

    def textFileToIndraFile(textFile: File, indraDir: String): File = {
      val name = textFile.getName
      val index = name.indexOf('_')
      val prefix = name.slice(0, index)

      new File(indraDir + "/" + prefix + "_success.json")
    }

    for (textFile <- textFiles) {
      val indraFilename = indraDir + "/" + textFile.getName()
      val indraFile = textFileToIndraFile(textFile, indraDir)

      if (indraFile.exists()) {
        val text = getContents(textFile)
        val indra = getContents(indraFile)

        addDocument(writer, text, indra)
      }
    }
    writer.close()
  }

  protected def addDocument(writer: IndexWriter, text: String, indra: String): Unit = {
    val document = new Document

    document.add(new TextField("text", text, Field.Store.YES))
    document.add(new StoredField("indra", indra))
    writer.addDocument(document)
  }
}
