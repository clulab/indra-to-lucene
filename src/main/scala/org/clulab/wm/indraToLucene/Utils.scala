package org.clulab.wm.indraToLucene

import java.io._

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Utils {
  type FileMapping = (File, String) => File

  def getContents(file: File) = {
    val source = Source.fromFile(file)
    val rawText = source.getLines.mkString("\n")

    source.close()
    rawText
  }

  /** Recursively finds all files with the given extension in the given directory */
  def findFiles(dir: String, ext: String): List[File] = {
    val files = new ListBuffer[File]

    // find all files ending with ext in this directory
    val fileNameFilter = new FilenameFilter {
      override def accept(file: File, name: String): Boolean = {
        name.toLowerCase.endsWith("." + ext)
      }
    }
    files ++= new File(dir).listFiles(fileNameFilter).toList

    // recursive call
    val dirNameFilter = new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = {
        val file = new File(dir.getAbsolutePath + File.separator + name)
        file.isDirectory
      }
    }
    val subdirs = new File(dir).listFiles(dirNameFilter)
    for (subdir <- subdirs) {
      files ++= findFiles(subdir.getAbsolutePath, ext)
    }

    files.toList
  }
}
