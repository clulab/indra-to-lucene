package org.clulab.wm.indraToLucene;

import org.clulab.wm.indraToLucene.Searcher;
import org.clulab.wm.indraToLucene.SearchResult;

public class Sample {

	public static void main(String[] args) {
		Searcher searcher = new Searcher("../index52");
		SearchResult[] results = searcher.search("famine AND hunger", 10);

		for (SearchResult result: results)
			System.out.println(result.textName() + "\t" + result.indraName());
		searcher.close();
	}
}
