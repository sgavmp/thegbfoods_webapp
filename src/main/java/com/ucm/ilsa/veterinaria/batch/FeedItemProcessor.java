package com.ucm.ilsa.veterinaria.batch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.CharsetEnum;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedPlaceEnum;
import com.ucm.ilsa.veterinaria.domain.FeedTypeEnum;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.service.impl.FeedScrapingImpl;

public class FeedItemProcessor implements ItemProcessor<Feed, Feed> {

	private final static Logger LOGGER = Logger
			.getLogger(FeedItemProcessor.class);

	private Directory allDirectory;
	private IndexWriter allIndex;
	private IndexReader readAllIndex;

	public FeedItemProcessor() {
		try {
			this.allDirectory = FSDirectory.open(new File("/gb/news/all")
					.toPath());

			this.allIndex = new IndexWriter(allDirectory,
					new IndexWriterConfig(getAnalyzer()));
			this.readAllIndex = DirectoryReader.open(allDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Analyzer getAnalyzer() {
		Map<String, Analyzer> aMap = new HashMap<String, Analyzer>();
		Analyzer sa = new StandardAnalyzer(CharArraySet.EMPTY_SET);
		aMap.put(News.fieldTitleNoCase, sa);
		aMap.put(News.fieldTitle, new WhitespaceAnalyzer());
		aMap.put(News.fieldBodyNoCase, sa);
		aMap.put(News.fieldBody, new WhitespaceAnalyzer());
		aMap.put(News.fieldUrl, new WhitespaceAnalyzer());
		aMap.put(News.fieldSite, new WhitespaceAnalyzer());
		return new PerFieldAnalyzerWrapper(sa, aMap);
	}

	@Override
	public Feed process(Feed item) throws Exception {
//		item.setFeedPlace(FeedPlaceEnum.general);
//		item.setFeedType(FeedTypeEnum.general);
//		allIndex.updateNumericDocValue(new Term(News.fieldSite,item.getId().toString()), News.fieldSiteType, FeedTypeEnum.general.getValue());
//		allIndex.updateNumericDocValue(new Term(News.fieldSite,item.getId().toString()), News.fieldSiteLoc, FeedPlaceEnum.general.getValue());
		List<String> urls = Lists.newArrayList();
		for (int i=0; i<readAllIndex.maxDoc(); i++) {
		    Document doc = readAllIndex.document(i);
		    doc.add(new TextField("titleN", doc.get(News.fieldTitle), Field.Store.NO));
		    doc.add(new TextField("bodyN", doc.get(News.fieldBody), Field.Store.NO));
		    String url = doc.get("id").toString();
		    if (!urls.contains(url)) {
		    	allIndex.deleteDocuments(new Term("id",url));
		    	allIndex.updateDocument(new Term("id",url), doc);
//		    	allIndex.updateDocument(term, doc);(doc);
		    	urls.add(url);
		    }
		}
		allIndex.commit();
		return item;
	}

	

}
