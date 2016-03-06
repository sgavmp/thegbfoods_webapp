package es.ucm.visavet.gbf.domains.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FPattern implements Filter {
  private Pattern pattern;
  public FPattern(String patternDesc) {
    patternDesc = patternDesc.replace("-","\\-");
    patternDesc = patternDesc.replace(".","\\.");
    patternDesc = patternDesc.replace("*","(.)*");
    try {
      this.pattern = Pattern.compile(patternDesc);    
    }
    catch(Exception e) {
       e.printStackTrace();        
    } 
  }
  public boolean eval(String domain) {
    return pattern.matcher(domain).matches();  
  }
  public String toString() {
    return pattern.toString();  
  }
}
