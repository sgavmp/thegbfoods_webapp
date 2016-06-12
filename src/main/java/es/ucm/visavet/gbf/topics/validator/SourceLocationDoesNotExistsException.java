package es.ucm.visavet.gbf.topics.validator;

public class SourceLocationDoesNotExistsException extends ParseException {
 private String loc;
 public SourceLocationDoesNotExistsException(String loc){
   super();
   this.loc = loc;
 }
 public String getLoc() {return loc;};
 public String toString() {
   return "La localizacón de fuente "+loc+" no existe (no está definido)";
 }    
}
