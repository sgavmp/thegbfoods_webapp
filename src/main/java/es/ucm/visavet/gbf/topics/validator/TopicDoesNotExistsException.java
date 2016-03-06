package es.ucm.visavet.gbf.topics.validator;

public class TopicDoesNotExistsException extends ParseException {
 private String topic;
 public TopicDoesNotExistsException(String topic){
   super();
   this.topic = topic;
 }
 public String getTopic() {return topic;};
 public String toString() {
   return "El tópico "+topic+" no existe (no está definido)";
 }    
}
