package es.ucm.visavet.gbf.domains.filter;

public class FOr implements Filter{
  private Filter op1;
  private Filter op2;
  public FOr(Filter op1, Filter op2) {
    this.op1=op1;
    this.op2=op2;
  }
  public boolean eval(String domain) {
    return op1.eval(domain) || op2.eval(domain);
  }  
  public String toString() {
    return "or{"+op1.toString()+","+op2.toString()+"}";            
  }

}
