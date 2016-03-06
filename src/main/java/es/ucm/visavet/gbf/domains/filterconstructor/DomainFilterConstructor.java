/* Generated By:JavaCC: Do not edit this line. DomainFilterConstructor.java */
package es.ucm.visavet.gbf.domains.filterconstructor;
import es.ucm.visavet.gbf.domains.filter.Filter;
import java.io.InputStream;
public class DomainFilterConstructor implements DomainFilterConstructorConstants {
    private DomainFilterConstructorSemantics sem;
    public DomainFilterConstructor(DomainFilterConstructorSemantics sem, InputStream input) {
          this(input);
          this.sem = sem;
        }

  final public Filter filter() throws ParseException {
                     Filter f;
    f = filter0();
    jj_consume_token(0);
                                                    {if (true) return f;}
    throw new Error("Missing return statement in function");
  }

  final public Filter filter0() throws ParseException {
                     Filter f1,f2;
    f1 = filter1();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PATTERN:
      case OR:
      case NOT:
      case 8:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR:
        jj_consume_token(OR);
        break;
      default:
        jj_la1[1] = jj_gen;
        ;
      }
      f2 = filter1();
                                                                         f1=sem.buildOrFilter(f1,f2);
    }
                                                                                                          {if (true) return f1;}
    throw new Error("Missing return statement in function");
  }

  final public Filter filter1() throws ParseException {
                     Filter f1,f2;
    f1 = filter2();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      jj_consume_token(AND);
      f2 = filter2();
                                                                       f1=sem.buildAndFilter(f1,f2);
    }
                                                                                                          {if (true) return f1;}
    throw new Error("Missing return statement in function");
  }

  final public Filter filter2() throws ParseException {
                     Filter f;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOT:
      jj_consume_token(NOT);
      f = filter2();
                                                    {if (true) return sem.buildNotFilter(f);}
      break;
    case PATTERN:
    case 8:
      f = filter3();
                                                                                                  {if (true) return f;}
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public Filter filter3() throws ParseException {
                     Token tk; Filter f;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PATTERN:
      tk = jj_consume_token(PATTERN);
                                       {if (true) return sem.buildPatternFilter(tk.image);}
      break;
    case 8:
      jj_consume_token(8);
      f = filter0();
      jj_consume_token(9);
                                           {if (true) return f;}
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  public DomainFilterConstructorTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[5];
  static private int[] jj_la1_0;
  static {
      jj_la1_0();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0x11a,0x8,0x4,0x112,0x102,};
   }

  public DomainFilterConstructor(java.io.InputStream stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new DomainFilterConstructorTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  public DomainFilterConstructor(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new DomainFilterConstructorTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  public DomainFilterConstructor(DomainFilterConstructorTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  public void ReInit(DomainFilterConstructorTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[10];
    for (int i = 0; i < 10; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 5; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 10; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

  }