package es.ucm.visavet.gbf.topics.validator.test;

import es.ucm.visavet.gbf.topics.manager.ITopicsManager;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

class TestTopicsManager implements ITopicsManager {
  public boolean existsTopic(String topic) {
    switch(topic) {
        case "Enfermedad": return true;
        case "Dolencia": return true;
        case "Desesperación": return true;
        default: return false;    
    }  
  }
  public Set<String> getDependencies(String topic) {
    Set dEnfermedad = new HashSet<String>();
      dEnfermedad.add("Dolencia");
    Set dDolencia = new HashSet<String>();
      dDolencia.add("Desesperación");
    Set dDesesperacion = new HashSet<String>();      
    switch(topic) {
        case "Enfermedad": return dEnfermedad;
        case "Dolencia": return dDolencia;
        case "Desesperación": return dDesesperacion;
        default: return new HashSet<String>();    
    }
  } 
  public void addDependency(String topic, String ofTopic) {
     System.out.println(topic+"--->"+ofTopic);  
   }       
  public InputStream getDefinition(String topic) {return null;}  
}

public class Test {
   public static void main(String[] args) throws Exception {
      TopicValidator validator = new TopicValidator(new TopicValidatorSemantics("Desesperación",new TestTopicsManager()),
                                                    new FileInputStream(args[0]));
      validator.topic();
   } 
    
}
