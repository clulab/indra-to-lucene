# indra-to-lucene
Converts INDRA statements into a Lucene index

The software is also distributed as a fat jar, which includes all the required libraries except those of Java itself.  Lucene and Scala are included.  API access to search results is demonstrated in the sample Java application, included with the source and shown below.
```
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
```

The default Scala application, SearcherApp, will run by default if the jar file is executed.  It may provide useful diagnostic output.
```
> java -jar '.\indra-to-lucene-assembly-0.1.jar'
Syntax: org.clulab.wm.indraToLucene.apps.SearcherApp$ indexDir "query" [maxHits]

> java -jar '.\indra-to-lucene-assembly-0.1.jar' ../index52 "hunger AND famine" 1
0.20504367      32_sudantribune.txt     32_success.json ...
```
