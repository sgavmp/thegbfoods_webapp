package es.ucm.visavet.gbf.domains.filter;

public class FNot implements Filter{
  private Filter op;
  public FNot(Filter op) {
    this.op=op;
  }
  public boolean eval(String domain) {
    return ! op.eval(domain);
  }
  public String toString() {
    return "!"+op.toString();            
  }
}
