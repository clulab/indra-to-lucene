package org.clulab.wm.indraToLucene.apps

import org.clulab.wm.indraToLucene.Merger

object MergerApp extends App {
  if (args.size != 2) {
    println("Syntax: " + this.getClass().getName() + " wideFile thinFile")
    System.exit(-1)
  }

  new Merger().merge(args(0), args(1))
}
