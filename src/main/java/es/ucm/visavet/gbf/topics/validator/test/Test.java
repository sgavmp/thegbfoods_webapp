package es.ucm.visavet.gbf.topics.validator.test;

import es.ucm.visavet.gbf.topics.manager.ITopicsManager;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;


class TestTopicsManager implements ITopicsManager {
    
  public boolean existsSourceType(String type) {
      switch(type) {
       case "per�odico": return true;
       case "revista": return true;
       default: return false;       
      } 
  }
  public boolean existsSourceLocation(String type) {
      switch(type) {
       case "es": return true;
       case "usa": return true;
       default: return false;       
      } 
  }
   
  public boolean existsTopic(String topic) {
    switch(topic) {
        case "Enfermedad": return true;
        case "Dolencia": return true;
        case "Desesperaci�n": return true;
        default: return false;    
    }  
  }
  public Set<String> getDependencies(String topic) {
    Set dEnfermedad = new HashSet<String>();
      dEnfermedad.add("Dolencia");
    Set dDolencia = new HashSet<String>();
      dDolencia.add("Desesperaci�n");
    Set dDesesperacion = new HashSet<String>();      
    switch(topic) {
        case "Enfermedad": return dEnfermedad;
        case "Dolencia": return dDolencia;
        case "Desesperaci�n": return dDesesperacion;
        default: return new HashSet<String>();    
    }
  } 
  public void addDependency(String topic, String ofTopic) {
     System.out.println(topic+"--->"+ofTopic);  
   }       
  public InputStream getDefinition(String topic) {return null;}  

    @Override
    public int getSourceType(String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSourceLocation(String type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

public class Test {
   public static void main(String[] args) throws Exception {
	  String topic = "\"lengua azul\"  | triquinosis - ('fiebre porciona' & gripe  | fr�o - calor - �o�er�A & ambig�eDa  AMbiG�ED�D676 )      | Enfermedad | @Tper�odico | @Lfr";
	  InputStream stream = new ByteArrayInputStream(topic.getBytes());
      TopicValidator validator = new TopicValidator(new TopicValidatorSemantics("Desesperaci�n",new TestTopicsManager()),
                                                    new FileInputStream(args[0]));
      validator.topic();
   } 
    
}
