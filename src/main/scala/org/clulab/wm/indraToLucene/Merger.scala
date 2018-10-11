package org.clulab.wm.indraToLucene

import java.io.File

class Merger {

  def merge(wideFileName: String, thinFileName: String): Unit = {
    val wideLines = Utils.getContents(new File(wideFileName)).split('\n').sortBy(line => line.split('\t')(2))
    val thinLines = Utils.getContents(new File(thinFileName)).split('\n').sortBy(line => line.split('\t')(2))
    var wideIndex = 0
    var thinIndex = 0

    while (wideIndex < wideLines.size && thinIndex < thinLines.size) {
      val wideFields = wideLines(wideIndex).split('\t')
      val thinFields = thinLines(thinIndex).split('\t')

      if (wideFields(2) == thinFields(2)) {
//        print("=\t")
        println(wideLines(wideIndex))
        wideIndex += 1
        thinIndex += 1
      }
      else if (wideFields(2) <= thinFields(2)) {
//        print("w\t")
        println(wideLines(wideIndex))
        wideIndex += 1
      }
      else {
//        print("t\t")
        print(thinLines(thinIndex))
        println("\t")
        thinIndex += 1
      }
    }

    if (wideIndex < wideLines.size)
      while (wideIndex < wideLines.size) {
//        print("w\t")
        println(wideLines(wideIndex))
        wideIndex += 1
      }
    else if (thinIndex < thinLines.size) {
//      print("t\t")
      print(thinLines(thinIndex))
      println("\t")
      thinIndex += 1
    }
  }
}
