package es.ucm.visavet.gbf.domains.validator.test;

import java.io.FileInputStream;
import es.ucm.visavet.gbf.domains.validator.*;

public class Test {
   public static void main(String[] args) throws Exception {
     DomainFilterValidator validator = new DomainFilterValidator(new FileInputStream("input.txt"));
     validator.constraint();
   } 
    
}
