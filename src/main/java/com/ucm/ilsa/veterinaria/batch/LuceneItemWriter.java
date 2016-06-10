package com.ucm.ilsa.veterinaria.batch;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.NativeFSLockFactory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;

public class LuceneItemWriter implements ItemWriter<List<News>> {
	
	private final static Logger LOGGER = Logger
			.getLogger(LuceneItemWriter.class);
	
	private FSDirectory allDirectory;
	private IndexWriter allIndex;

	@Autowired(required=true)
	public NewsIndexService newsIndexService;
	
//	public LuceneItemWriter() {
//		try {
//			allDirectory = SimpleFSDirectory.open(new File("/gb/news/all/").toPath());
//			IndexWriterConfig config = new IndexWriterConfig(getAnalyzer());
//			allIndex = new IndexWriter(allDirectory,config);
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}

	@Override
	public void write(List<? extends List<News>> items) throws Exception {
		for (List<News> item : items) {
			LOGGER.info("Se van a almacenar " + item.size());
			newsIndexService.indexNews(item);
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
	
	private void addDocument(News news) throws Exception {
		Document d = new Document();
		d.add(new StringField("id", news.getUrl(), Field.Store.YES));
		d.add(new TextField("title", news.getTitle(), Field.Store.YES));
		d.add(new TextField("titleN", news.getTitle(), Field.Store.NO));
		d.add(new TextField("body", news.getContent(), Field.Store.YES));
		d.add(new TextField("bodyN", news.getContent(), Field.Store.NO));
		d.add(new StringField("datePub", DateTools.dateToString(
				news.getPubDate(), Resolution.MINUTE), Field.Store.YES));
		d.add(new StringField("feed", news.getSite().toString(),
				Field.Store.YES));
		d.add(new StringField("dateCreate", DateTools.dateToString(new Date(),
				Resolution.MINUTE), Field.Store.YES));
		allIndex.addDocument(d);
	}

}
