
package es.ucm.visavet.gbf.topics.queryconstructor;

import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import es.ucm.visavet.gbf.topics.manager.ITopicsManager;

public class QueryConstructorSemantics {
   private ITopicsManager topicsManager;
   private String fieldCaseSensitive;
   private String fieldCaseNonsensitive;
   private String fieldSourceLocation;
   private String fieldSourceType;
   
   public QueryConstructorSemantics(ITopicsManager topicsManager,
                                    String fieldCaseSensitive,
                                    String fieldCaseNonsensitive,
                                    String fieldSourceLocation,
                                    String fieldSourceType) {
     this.topicsManager = topicsManager;
     this.fieldCaseSensitive = fieldCaseSensitive;
     this.fieldCaseNonsensitive = fieldCaseNonsensitive;
     this.fieldSourceLocation = fieldSourceLocation;
     this.fieldSourceType = fieldSourceType;
   }         
   
   public Query buildTermQuery(String term) {
     if (term.charAt(0)=='\'') {
      return buildPhraseQuery(term.substring(1,term.length()-1).toLowerCase(),fieldCaseNonsensitive);   
     }
     return new TermQuery(new Term(fieldCaseNonsensitive,term.toLowerCase()));
   } 
   
   public Query buildLiteralTermQuery(String term) {
     term = term.substring(1,term.length()-1);   
     return buildPhraseQuery(term,fieldCaseSensitive);   
   } 
   
   public Query buildTopicRefQuery(String term) throws ParseException {
     Reader topicStream;
	try {
		topicStream = topicsManager.getDefinition(term.substring(1));
		QueryConstructor queryConstructor = new QueryConstructor(this,topicStream);
		return queryConstructor.topic();
	} catch (UnsupportedEncodingException e) {
		throw new ParseException("Error de codificación");
	}
     
     
   } 
   
   public Query buildSourceTypeQuery(String type) throws ParseException {
     String code = new Integer(topicsManager.getSourceType(type.substring(2))).toString();  
     return new TermQuery(new Term(fieldSourceType,code));
   }
   
   public Query buildSourceLocQuery(String loc) throws ParseException {
     String code = new Integer(topicsManager.getSourceLocation(loc.substring(2))).toString();  
     return new TermQuery(new Term(fieldSourceLocation,code));
   }
   
   public Query buildDifQuery(Query q1,Query q2) {
     BooleanClause op1 = new BooleanClause(q1,BooleanClause.Occur.MUST);  
     BooleanClause op2 = new BooleanClause(q2,BooleanClause.Occur.MUST_NOT);
     BooleanQuery.Builder b = new BooleanQuery.Builder();
     b.add(op1);
     b.add(op2);
     return b.build();     
   } 
   public Query buildAndQuery(Query q1,Query q2) {
     BooleanClause op1 = new BooleanClause(q1,BooleanClause.Occur.MUST);  
     BooleanClause op2 = new BooleanClause(q2,BooleanClause.Occur.MUST);
     BooleanQuery.Builder b = new BooleanQuery.Builder();
     b.add(op1);
     b.add(op2);
     return b.build();     
   } 
   public Query buildOrQuery(Query q1,Query q2) {
     BooleanClause op1 = new BooleanClause(q1,BooleanClause.Occur.SHOULD);  
     BooleanClause op2 = new BooleanClause(q2,BooleanClause.Occur.SHOULD);
     BooleanQuery.Builder b = new BooleanQuery.Builder();
     b.add(op1);
     b.add(op2);
     return b.build();     
   }     
   private Query buildPhraseQuery(String phrase,String field) {
      String[] terms = phrase.split("\\s+");
      PhraseQuery.Builder qb = new PhraseQuery.Builder();
      for(String term: terms) {
          qb.add(new Term(field,term));
      }
      return qb.build();
   } 
}