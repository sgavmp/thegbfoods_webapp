/* Generated By:JavaCC: Do not edit this line. TopicValidator.java */
package es.ucm.visavet.gbf.topics.validator;
import java.io.InputStream;
public class TopicValidator implements TopicValidatorConstants {
    private TopicValidatorSemantics sem;
    public TopicValidator(TopicValidatorSemantics sem, InputStream input) {
          this(input);
          this.sem = sem;
        }

  final public void topic() throws ParseException {
    topic0();
    jj_consume_token(0);
  }

  final public void topic0() throws ParseException {
    topic1();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TERM:
      case LITERAL_TERM:
      case TOPIC_NAME:
      case OR:
      case ATTYPE:
      case ATLOC:
      case 13:
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
      topic1();
    }
  }

  final public void topic1() throws ParseException {
    topic2();
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
      topic2();
    }
  }

  final public void topic2() throws ParseException {
    topic3();
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DIF:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_3;
      }
      jj_consume_token(DIF);
      topic3();
    }
  }

  final public void topic3() throws ParseException {
                  Token tk;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TERM:
      jj_consume_token(TERM);
      break;
    case LITERAL_TERM:
      jj_consume_token(LITERAL_TERM);
      break;
    case ATTYPE:
      tk = jj_consume_token(ATTYPE);
                                     sem.validateSourceType(tk.image);
      break;
    case ATLOC:
      tk = jj_consume_token(ATLOC);
                                        sem.validateSourceLocation(tk.image);
      break;
    case TOPIC_NAME:
      tk = jj_consume_token(TOPIC_NAME);
                                        sem.validateReferenceToTopic(tk.image);
      break;
    case 13:
      jj_consume_token(13);
      topic0();
      jj_consume_token(14);
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  public TopicValidatorTokenManager token_source;
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
      jj_la1_0 = new int[] {0x21ae,0x20,0x10,0x40,0x218e,};
   }

  public TopicValidator(java.io.InputStream stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new TopicValidatorTokenManager(jj_input_stream);
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

  public TopicValidator(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new TopicValidatorTokenManager(jj_input_stream);
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

  public TopicValidator(TopicValidatorTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  public void ReInit(TopicValidatorTokenManager tm) {
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
    boolean[] la1tokens = new boolean[15];
    for (int i = 0; i < 15; i++) {
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
    for (int i = 0; i < 15; i++) {
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
