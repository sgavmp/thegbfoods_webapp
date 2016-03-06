package es.ucm.visavet.gbf.domains.filterconstructor.test;

import es.ucm.visavet.gbf.domains.filter.Filter;
import es.ucm.visavet.gbf.domains.filterconstructor.*;
import java.io.FileInputStream;

public class Test {
   public static void main(String[] args) throws Exception {
     DomainFilterConstructor filter = new DomainFilterConstructor(new DomainFilterConstructorSemantics(),new FileInputStream(args[0]));
     Filter f = filter.filter();
     System.out.println(f.eval("www.google.com"));
     System.out.println(f.eval("www.ucm.es"));
     System.out.println(f.eval("www.fi.upm.es"));
     System.out.println(f.eval("ilsa.fdi.ucm.es"));
   } 
    
}
