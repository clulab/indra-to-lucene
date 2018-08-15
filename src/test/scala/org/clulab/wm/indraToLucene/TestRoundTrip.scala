package org.clulab.wm.indraToLucene

import java.io.File

class TestRoundTrip extends Test {

  protected def find(needle: String, haystack: String): Boolean =
      haystack.toLowerCase.contains(needle.toLowerCase)

  val TEXT_DIR = TestUtils.resourceNameToFileName("/text").replace('\\', '/')
  val INDRA_DIR = ""
  val INDEX_DIR = "./index"

  println("TEXT_DIR: " + TEXT_DIR)

  // Since we don't have the indra files, just record the text file again.
  protected def mappingIdentity(textFile: File, indraDir: String): File = textFile

  val indexFile = new File(INDEX_DIR)
  indexFile.mkdirs()
  TestUtils.deleteDirectoryRecursion(indexFile)
  indexFile.mkdirs()

  new Indexer().index(TEXT_DIR, INDRA_DIR, INDEX_DIR, mappingIdentity)

  behavior of "the searcher"

  val searcher = new Searcher(INDEX_DIR)

  it should "use keywords properly" in {
    val term = "hunger"
    val results = searcher.search(term)

    results.size should be (20)
    results.foreach { result =>
      find(term, result.text) should be (true)
      find(term, result.indra) should be (true)
    }
  }

  it should "use OR properly" in {
    val leftTerm = "poverty"
    val rightTerm = "shortages"
    val term = leftTerm + " OR " + rightTerm

    val results = searcher.search(term)

    results.size should be (23)
    results.foreach { result =>
      (find(leftTerm, result.text) || find(rightTerm, result.text)) should be (true)
    }
  }

  it should "use AND properly" in {
    val leftTerm = "poverty"
    val rightTerm = "shortages"
    val term = leftTerm + " AND " + rightTerm

    val results = searcher.search(term)

    // This should be less than the OR search.
    results.size should be (6)
    results.foreach { result =>
      (find(leftTerm, result.text) && find(rightTerm, result.text)) should be (true)
    }
  }

  it should "use NOT properly" in {
    val leftTerm = "poverty"
    val rightTerm = "shortages"
    val leftNotRightResults = searcher.search(leftTerm + " NOT " + rightTerm)
    val rightNotLeftResults = searcher.search(rightTerm + " NOT " + leftTerm)

    // These should make up the different between AND and OR results.
    leftNotRightResults.size + rightNotLeftResults.size should be (23 - 6)

    leftNotRightResults.foreach { result =>
      (find(leftTerm, result.text) && !find(rightTerm, result.text))  should be (true)
    }
    rightNotLeftResults.foreach { result =>
      (find(rightTerm, result.text) && !find(leftTerm, result.text))  should be (true)
    }
  }
}
