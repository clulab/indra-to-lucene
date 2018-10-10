package org.clulab.wm.indraToLucene

import java.io.File

import scala.collection.mutable

class Mapper {

  def afterLast(string: String, char: Char, all: Boolean = true): String = {
    val index = string.lastIndexOf(char)

    if (index < 0)
      if (all) string
      else ""
    else string.substring(index + 1)
  }

  def before(string: String, index: Int, all: Boolean): String = {
    if (index < 0)
      if (all) string
      else ""
    else string.substring(0, index)
  }

  def beforeLast(string: String, char: Char, all: Boolean = true): String =
      before(string, string.lastIndexOf(char), all)

  def beforeFirst(string: String, char: Char, all: Boolean = true): String =
      before(string, string.indexOf(char), all)

  protected case class SideInfo(year: String, kind: String, url: String, title: String)

  protected def getSideInfos(sideFileName: String): mutable.Map[String, SideInfo] = {
    def toKey(url: String): String = beforeLast(afterLast(url, '/'), '.')

    val file = new File(sideFileName)
    val map = new mutable.HashMap[String, SideInfo]()

    if (file.exists()) {
      val content = Utils.getContents(file)
      val lines = content.split('\n')

      lines.foreach { line =>
        val values = line.split('\t')

        require(values.size == 4)

        val year = values(0)
        val kind = values(1)
        val url = values(2)
        val title = values(3)
        val sideInfo = SideInfo(year, kind, url, title)
        val key = toKey(url)

        map(key)=sideInfo
      }
    }
    map
  }

  protected def jsonldMapping(textFileName: String, jsonldDir: String): File = {
    val jsonldFileName = jsonldDir + "/" + textFileName + ".jsonld"

    new File(jsonldFileName)
  }

  protected def metaMapping(textFileName: String, metaDir: String): File = {
    val metaFileName = metaDir + "/" + beforeLast(afterLast(textFileName, '_'), '.') + ".json"

    new File(metaFileName)
  }

  protected def indraMapping(textFileName: String): String = textFileName + ".jsonld"

  protected def sideMapping(textFileName: String): String =
      beforeLast(afterLast(textFileName, '_'), '.')

  def map(textDir: String, jsonldDir: String, metaDir: String, sideFileName: String): Unit = {
    val sideInfos = getSideInfos(sideFileName)
    val textFiles = Utils.findFiles(textDir, "txt")

    println("textFile\tjsonldFile\tmetaFile\tindraId\tyear\tkind\turl\ttitle")
    for (textFile <- textFiles) {
      val textFileName = textFile.getName()
      val jsonldFile = jsonldMapping(textFileName, jsonldDir)
      val metaFile = metaMapping(textFileName, metaDir)
      val indraId = indraMapping(textFileName)
      val sideKey = sideMapping(textFileName)

      print(textFile.getName())
      print("\t")
      print(if (jsonldFile.exists()) jsonldFile.getName() else "")
      print("\t")
      print(if (metaFile.exists()) metaFile.getName() else "")
      print("\t")
      print(if (jsonldFile.exists()) indraId else "")
      print("\t")
      if (sideInfos.contains(sideKey)) {
        val sideInfo = sideInfos(sideKey)

        print(sideInfo.year)
        print("\t")
        print(sideInfo.kind)
        print("\t")
        print(sideInfo.url)
        print("\t")
        print(sideInfo.title)
      }
      else
        print("\t\t\t")
      println()
    }
  }
}
