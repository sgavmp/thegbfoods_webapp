package es.ucm.visavet.gbf.topics.queryconstructor.test;

import es.ucm.visavet.gbf.topics.manager.ITopicsManager;
import es.ucm.visavet.gbf.topics.queryconstructor.QueryConstructor;
import es.ucm.visavet.gbf.topics.queryconstructor.QueryConstructorSemantics;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.ucm.ilsa.veterinaria.domain.Topic;



class TopicsManager implements ITopicsManager {
   public boolean existsTopic(String topic) {return false;}
   public Set<String> getDependencies(String topic) {return null;} 
   public void addDependency(String topic, String ofTopic) {}  
   public InputStream getDefinition(String topic) {
   try{    
     switch(topic) {
         case "Enfermedad": return new FileInputStream("enfermedad.txt");   
         case "Dolor": return new FileInputStream("dolor.txt");   
     }
   }catch(Exception e) {
     System.err.println("ERROR AL ABRIR FICHERO DE TOPIC "+topic);
     System.exit(1);
   }
   return null;
  }        

    @Override
    public boolean existsSourceType(String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean existsSourceLocation(String location) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSourceType(String type) {
        return 567;
    }

    @Override
    public int getSourceLocation(String type) {
       return 905; 
    }
	@Override
	public Topic getTopic(String topic) {
		// TODO Auto-generated method stub
		return null;
	}
 }


public class Test {
   public static void main(String[] args) throws Exception {    
      String casesensitiveField = "bodycs";
      String casenonsensitiveField = "bodycns"; 
      String sourceLocField = "sloc"; 
      String typeLocField = "stype"; 
      QueryConstructor queryConstructor = new QueryConstructor(new QueryConstructorSemantics(new TopicsManager(),casesensitiveField,
                                                               casenonsensitiveField,sourceLocField, typeLocField),
                                                               new FileInputStream(args[0]));
      Query q = queryConstructor.topic();
      System.out.println(q);
      Directory index = new RAMDirectory();
      IndexWriterConfig config = new IndexWriterConfig(makeAnalyzer(casesensitiveField,casenonsensitiveField));
      IndexWriter w = new IndexWriter(index, config);      
      addDocument(w,"El primer documento sobre la fiebre porcina. La fiebre porcina es muy dañina",casesensitiveField,casenonsensitiveField);
      addDocument(w,"El primer documento sobre la fiebre porcina",casesensitiveField,casenonsensitiveField);
      w.close();
      search(q,index,casesensitiveField);
   } 
   
   private static Analyzer makeAnalyzer(String caseSensitiveField, String caseNonsensitiveField) {
     Map aMap = new HashMap();
     Analyzer sa = new StandardAnalyzer(CharArraySet.EMPTY_SET);
     aMap.put(caseNonsensitiveField,sa);
     aMap.put(caseSensitiveField,new WhitespaceAnalyzer());
     return new PerFieldAnalyzerWrapper(sa,aMap); 
   }
   private static void addDocument(IndexWriter w, String t, String csf, String cnsf) throws Exception {
     Document d = new Document();
     d.add(new TextField(csf, t, Field.Store.YES));
     d.add(new TextField(cnsf, t, Field.Store.YES));
     w.addDocument(d);
   }
   private static void search(Query q, Directory index, String field) throws Exception {
     IndexReader reader = DirectoryReader.open(index);
     IndexSearcher searcher = new IndexSearcher(reader);
     TopDocs docs = searcher.search(q, Integer.MAX_VALUE);
     ScoreDoc[] hits = docs.scoreDocs;
     for(int i=0;i<hits.length;++i) {
       int docId = hits[i].doc;
       Document d = searcher.doc(docId);
       System.out.println((i + 1) + ".[ "+hits[i].score+"]"+d.get(field));
     }
   }
}