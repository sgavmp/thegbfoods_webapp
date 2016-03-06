package es.ucm.visavet.gbf.domains.filterconstructor;

import es.ucm.visavet.gbf.domains.filter.FAnd;
import es.ucm.visavet.gbf.domains.filter.FNot;
import es.ucm.visavet.gbf.domains.filter.FOr;
import es.ucm.visavet.gbf.domains.filter.FPattern;
import es.ucm.visavet.gbf.domains.filter.Filter;

public class DomainFilterConstructorSemantics {
  public Filter buildOrFilter(Filter f1, Filter f2) {
    return new FOr(f1,f2);  
  }    
  public Filter buildAndFilter(Filter f1, Filter f2) {
    return new FAnd(f1,f2);  
  }    
  public Filter buildNotFilter(Filter f) {
    return new FNot(f);  
  }    
  public Filter buildPatternFilter(String p) {
    return new FPattern(p);  
  }    
}