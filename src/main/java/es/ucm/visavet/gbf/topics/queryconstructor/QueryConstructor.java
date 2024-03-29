/* QueryConstructor.java */
/* Generated By:JavaCC: Do not edit this line. QueryConstructor.java */
package es.ucm.visavet.gbf.topics.queryconstructor;
import java.io.Reader;

import org.apache.lucene.search.Query;

import es.ucm.visavet.gbf.topics.util.QueryUtil;
public class QueryConstructor implements QueryConstructorConstants {
    private QueryConstructorSemantics sem;
    public QueryConstructor(QueryConstructorSemantics sem, Reader input) {
          this(input);
          this.sem = sem;
        }

  final public Query topic() throws ParseException {Query q;
    q = topic0();
    jj_consume_token(0);
{if ("" != null) return q;}
    throw new Error("Missing return statement in function");
  }

  final public Query topic0() throws ParseException {Query q1,q2;
    q1 = topic1();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case TERM:
      case LITERAL_TERM:
      case TOPIC_NAME:
      case OR:
      case ATTYPE:
      case ATLOC:
      case 19:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OR:{
        jj_consume_token(OR);
        break;
        }
      default:
        jj_la1[1] = jj_gen;
        ;
      }
      q2 = topic1();
q1=sem.buildOrQuery(q1,q2);
    }
{if ("" != null) return q1;}
    throw new Error("Missing return statement in function");
  }

  final public Query topic1() throws ParseException {Query q1,q2;
    q1 = topic2();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case AND:{
        ;
        break;
        }
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      jj_consume_token(AND);
      q2 = topic2();
q1=sem.buildAndQuery(q1,q2);
    }
{if ("" != null) return q1;}
    throw new Error("Missing return statement in function");
  }

  final public Query topic2() throws ParseException {Query q1,q2;
    q1 = topic3();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DIF:{
        ;
        break;
        }
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      jj_consume_token(DIF);
      q2 = topic3();
q1=sem.buildDifQuery(q1,q2);
    }
{if ("" != null) return q1;}
    throw new Error("Missing return statement in function");
  }

  final public Query topic3() throws ParseException {Token tk; Query q;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case TERM:{
      tk = jj_consume_token(TERM);
{if ("" != null) return sem.buildTermQuery(QueryUtil.internize(tk.image));}
      break;
      }
    case LITERAL_TERM:{
      tk = jj_consume_token(LITERAL_TERM);
{if ("" != null) return sem.buildLiteralTermQuery(QueryUtil.internize(tk.image));}
      break;
      }
    case TOPIC_NAME:{
      tk = jj_consume_token(TOPIC_NAME);
{if ("" != null) return sem.buildTopicRefQuery(QueryUtil.internize(tk.image));}
      break;
      }
    case ATTYPE:{
      tk = jj_consume_token(ATTYPE);
{if ("" != null) return sem.buildSourceTypeQuery(QueryUtil.internize(tk.image));}
      break;
      }
    case ATLOC:{
      tk = jj_consume_token(ATLOC);
{if ("" != null) return sem.buildSourceLocQuery(QueryUtil.internize(tk.image));}
      break;
      }
    case 19:{
      jj_consume_token(19);
      q = topic0();
      jj_consume_token(20);
{if ("" != null) return q;}
      break;
      }
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public QueryConstructorTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[5];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x801ae,0x20,0x10,0x40,0x8018e,};
   }

  /** Constructor with InputStream. */
  public QueryConstructor(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public QueryConstructor(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new QueryConstructorTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public QueryConstructor(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new QueryConstructorTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
	if (jj_input_stream == null) {
      jj_input_stream = new SimpleCharStream(stream, 1, 1);
   } else {
      jj_input_stream.ReInit(stream, 1, 1);
   }
   if (token_source == null) {
      token_source = new QueryConstructorTokenManager(jj_input_stream);
   }

    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public QueryConstructor(QueryConstructorTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(QueryConstructorTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
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


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[21];
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
    for (int i = 0; i < 21; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  }
