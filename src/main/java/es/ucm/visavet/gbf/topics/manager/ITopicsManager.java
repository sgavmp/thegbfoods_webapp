package es.ucm.visavet.gbf.topics.manager;

import java.io.InputStream;
import java.util.Set;

import com.ucm.ilsa.veterinaria.domain.Topic;

public interface ITopicsManager {
   boolean existsTopic(String topic);
   boolean existsSourceType(String type);
   boolean existsSourceLocation(String location);
   int getSourceType(String type);
   int getSourceLocation(String location);
   Set<String> getDependencies(String topic); 
   void addDependency(String topic, String ofTopic); 
   InputStream getDefinition(String topic); 
   Topic getTopic(String topic);
}
