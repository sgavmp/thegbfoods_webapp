package com.ucm.ilsa.veterinaria.domain.topic;

import java.io.InputStream;
import java.util.Set;

public interface ITopicsManager {
   boolean existsTopic(String topic);
   Set<String> getDependencies(String topic); 
   void addDependency(String topic, String ofTopic); 
   InputStream getDefinition(String topic); 
}
