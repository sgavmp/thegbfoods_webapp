package es.ucm.visavet.gbf.topics.validator;

import es.ucm.visavet.gbf.topics.manager.ITopicsManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TopicValidatorSemantics {
  private ITopicsManager topicsManager;   
  private String definedTopic;
  public TopicValidatorSemantics(String definedTopic,ITopicsManager topicsManager) {
    this.definedTopic = definedTopic;
    this.topicsManager = topicsManager;
  }
  public void validateReferenceToTopic(String reference) throws ParseException {    
    String refName = reference.substring(1);
    if (! topicsManager.existsTopic(refName)) errorTopicDoesNotExists(reference);
    List<String >cyclicPath = new LinkedList<String>();
    cyclicPath.add(definedTopic);
    if (cyclicDependency(refName,cyclicPath)) errorCyclicDependency(cyclicPath);
    topicsManager.addDependency(definedTopic,refName);
  }   
  public void validateSourceType(String sourceType) throws ParseException {
    String type = sourceType.substring(2);
    if (! topicsManager.existsSourceType(type)) errorSourceTypeDoesNotExists(type);
  }
  public void validateSourceLocation(String sourceLoc) throws ParseException {
    String loc = sourceLoc.substring(2);
    if (! topicsManager.existsSourceLocation(loc)) errorSourceLocationDoesNotExists(loc);
  }
  private boolean cyclicDependency(String topic, List<String> path) {
    path.add(topic);  
    if (topic.equals(definedTopic)) return true;
    else {
       for(String referedTopic: topicsManager.getDependencies(topic)) {
	  if(cyclicDependency(referedTopic,path)) return true;
       }   
       path.remove(path.size()-1);
       return false;		   
    }
  }
  private void errorTopicDoesNotExists(String topic) throws ParseException {
    throw new TopicDoesNotExistsException(topic);   
  }
  private void errorCyclicDependency(List<String>cyclicPath) throws ParseException {
    throw new CyclicDependencyException(cyclicPath);  
  }
  private void errorSourceTypeDoesNotExists(String type) throws ParseException {
    throw new SourceTypeDoesNotExistsException(type);  
  }
  private void errorSourceLocationDoesNotExists(String loc) throws ParseException {
    throw new SourceLocationDoesNotExistsException(loc);  
  }
}
