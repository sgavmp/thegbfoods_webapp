package es.ucm.visavet.gbf.topics.manager;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import es.ucm.visavet.gbf.app.domain.Topic;

public interface ITopicsManager {
   boolean existsTopic(String topic);
   boolean existsSourceType(String type);
   boolean existsSourceLocation(String location);
   int getSourceType(String type);
   int getSourceLocation(String type);
   Set<String> getDependencies(String topic); 
   void addDependency(String topic, String ofTopic); 
   Reader getDefinition(String topic) throws UnsupportedEncodingException; 
   Topic getTopic(String topic);
}
