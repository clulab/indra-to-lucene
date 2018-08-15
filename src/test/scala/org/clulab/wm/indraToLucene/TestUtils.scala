package org.clulab.wm.indraToLucene

import java.io.{File, IOException}
import java.nio.file.Paths

import org.scalatest._

class Test extends FlatSpec with Matchers

object TestUtils {

  def resourceNameToFileName(resourceName: String): String = {
    val resource = this.getClass().getResource(resourceName)
    val file = Paths.get(resource.toURI()).toFile()

    file.getAbsolutePath
  }

  // https://softwarecave.org/2018/03/24/delete-directory-with-contents-in-java/
  def deleteDirectoryRecursion(file: File): Unit = {
    if (file.isDirectory) {
      val entries = file.listFiles

      if (entries != null)
        for (entry <- entries)
          deleteDirectoryRecursion(entry)
    }
    if (!file.delete)
      throw new IOException("Failed to delete " + file)
  }
}