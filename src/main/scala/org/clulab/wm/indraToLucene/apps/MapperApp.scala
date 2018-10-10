package org.clulab.wm.indraToLucene.apps

import org.clulab.wm.indraToLucene.{Indexer, Mapper}

object MapperApp extends App {
  val TEXT_DIR = "./text"
  val JSONLD_DIR = "./jsonld"
  val META_DIR = "./meta"

  val SIDE_FILE = "./doclist.txt"
  val MAP_FILE = "./map.tsv"

  if (args.size != 4) {
    println("Syntax: " + this.getClass().getName() + " textDir jsonldDir metaDir sideFile")
    System.exit(-1)
  }

  val textDir =
      if (args.size > 0) args(0)
      else TEXT_DIR
  val jsonldDir =
      if (args.size > 1) args(1)
      else JSONLD_DIR
  val metaDir =
      if (args.size > 2) args(2)
      else META_DIR
  val sideFileName =
      if (args.size > 3) args(3)
      else SIDE_FILE

  new Mapper().map(textDir, jsonldDir, metaDir, sideFileName)
}
