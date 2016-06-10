package es.ucm.visavet.gbf.topics.validator;

import java.util.Iterator;
import java.util.List;

public class CyclicDependencyException extends ParseException {    
   private List<String> cyclicPath;
   public CyclicDependencyException(List<String> cyclicPath) {
     this.cyclicPath = cyclicPath;  
   }
   public List<String> getCyclicPath() {return cyclicPath;}
   public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("Encontrada la siguiente dependencia cíclica entre tópicos: ");
    Iterator<String> it = cyclicPath.iterator();
    sb.append("#"+it.next());
    while(it.hasNext()) {
       String topic = it.next();        
       sb.append(" depende de #"+topic);
       if (it.hasNext())
       sb.append(", #"+topic);           
    }   
    return sb.toString();
   } 
}
