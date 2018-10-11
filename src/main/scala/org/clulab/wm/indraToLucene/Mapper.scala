package org.clulab.wm.indraToLucene

import java.io.{File, FileOutputStream, OutputStreamWriter, PrintWriter}
import java.nio.charset.StandardCharsets

import org.json4s.{JField, JObject, JString, JValue}
import org.json4s.jackson.JsonMethods.parse

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

  protected def getMetaValue(json: JValue, name: String): Option[String] = {
    val values: List[String] = for {
      JObject(child) <- json
      JField("MT", JObject(mt)) <- child
      JField("N", JString(n)) <- mt // name
      if n == name
      JField("V", JString(v)) <- mt // value
    } yield {
      v
    }
    values.headOption
  }

  protected def getMetaJson(file: File): Option[JValue] = {
    if (file.exists()) {
      val text = Utils.getContents(file)
      val json = parse(text)

      Some(json)
    }
    else
      None
  }

  protected def getTitle(file: File): String = {
    val jValue = getMetaJson(file)

    if (jValue.isDefined) {
      val title = getMetaValue(jValue.get, "title")

      title.getOrElse("")
    }
    else ""
  }

  protected def getSideInfos(sideFileName: String): mutable.Map[String, SideInfo] = {
    def toKey(url: String): String = beforeLast(afterLast(url, '/'), '.')

    val file = new File(sideFileName)
    val map = new mutable.HashMap[String, SideInfo]()

    if (file.exists()) {
      val content = Utils.getContents(file)
      val lines = content.split('\n')

      lines.foreach { line =>
        val values = line.split('\t')

        if (values.size != 3 && values.size != 4)
          println("bad")
        require(values.size == 3 || values.size == 4)

        val year = values(0)
        val kind = values(1)
        val url = values(2)
        val title = if (values.size > 3) values(3) else ""
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
//    val pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File("Map.txt")), StandardCharsets.UTF_8.toString))
    val pw = System.out
    val sideInfos = getSideInfos(sideFileName)
    val textFiles = Utils.findFiles(textDir, "txt")

    pw.println("textFile\tjsonldFile\tmetaFile\tindraId\tyear\tkind\turl\ttitle")
    for (textFile <- textFiles) {
      val textFileName = textFile.getName()
      val jsonldFile = jsonldMapping(textFileName, jsonldDir)
      val metaFile = metaMapping(textFileName, metaDir)
      val indraId = indraMapping(textFileName)
      val sideKey = sideMapping(textFileName)

      pw.print(textFile.getName())
      pw.print("\t")
      pw.print(if (jsonldFile.exists()) jsonldFile.getName() else "")
      pw.print("\t")
      pw.print(if (metaFile.exists()) metaFile.getName() else "")
      pw.print("\t")
      pw.print(if (jsonldFile.exists()) indraId else "")
      pw.print("\t")
      if (sideInfos.contains(sideKey)) {
        val sideInfo = sideInfos(sideKey)
        val title =
          if (!sideInfo.title.isEmpty) sideInfo.title
          else getTitle(metaFile)

        pw.print(sideInfo.year)
        pw.print("\t")
        pw.print(sideInfo.kind)
        pw.print("\t")
        pw.print(sideInfo.url)
        pw.print("\t")
        pw.print(title)
      }
      else
        pw.print("\t\t\t")
      pw.println()
    }
    pw.close()
  }
}
