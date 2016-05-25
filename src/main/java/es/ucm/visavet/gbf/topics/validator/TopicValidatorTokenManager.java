/* Generated By:JavaCC: Do not edit this line. TopicValidatorTokenManager.java */
package es.ucm.visavet.gbf.topics.validator;
import java.io.Reader;
import es.ucm.visavet.gbf.topics.util.QueryUtil;

public class TopicValidatorTokenManager implements TopicValidatorConstants
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 38:
         return jjStopAtPos(0, 4);
      case 40:
         return jjStopAtPos(0, 19);
      case 41:
         return jjStopAtPos(0, 20);
      case 45:
         return jjStopAtPos(0, 6);
      case 124:
         return jjStopAtPos(0, 5);
      default :
         return jjMoveNfa_0(11, 0);
   }
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
static final long[] jjbitVec0 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec2 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 59;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 11:
                  if ((0xffffdc3affffd8ffL & l) != 0L)
                  {
                     if (kind > 1)
                        kind = 1;
                     jjCheckNAddTwoStates(0, 1);
                  }
                  else if ((0x100002700L & l) != 0L)
                  {
                     if (kind > 9)
                        kind = 9;
                  }
                  else if (curChar == 34)
                     jjCheckNAddTwoStates(18, 19);
                  else if (curChar == 39)
                     jjCheckNAddTwoStates(13, 14);
                  if (curChar == 35)
                     jjCheckNAddTwoStates(22, 23);
                  break;
               case 0:
                  if ((0xffffdc3affffd8ffL & l) == 0L)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 2:
                  if (curChar != 41)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 3:
                  if (curChar != 40)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 5:
                  if (curChar != 45)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 7:
                  if (curChar != 38)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 9:
                  if (curChar != 34)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 10:
                  if (curChar != 39)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 12:
                  if (curChar == 39)
                     jjCheckNAddStates(0, 2);
                  break;
               case 14:
                  if ((0xffffff7fffffffffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 15:
                  if (curChar == 39 && kind > 1)
                     kind = 1;
                  break;
               case 16:
                  if (curChar == 34)
                     jjCheckNAddTwoStates(18, 19);
                  break;
               case 17:
                  if (curChar == 34)
                     jjCheckNAddStates(3, 5);
                  break;
               case 19:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     jjCheckNAddStates(3, 5);
                  break;
               case 20:
                  if (curChar == 34 && kind > 2)
                     kind = 2;
                  break;
               case 21:
                  if (curChar == 35)
                     jjCheckNAddTwoStates(22, 23);
                  break;
               case 22:
                  if ((0xffffdc3affffd8ffL & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 24:
                  if (curChar != 41)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 25:
                  if (curChar != 40)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 27:
                  if (curChar != 45)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 29:
                  if (curChar != 38)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 31:
                  if (curChar != 34)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 32:
                  if (curChar != 39)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 33:
                  if ((0x100002700L & l) != 0L && kind > 9)
                     kind = 9;
                  break;
               case 36:
                  if ((0xffffdc3affffd8ffL & l) == 0L)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 38:
                  if (curChar != 41)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 39:
                  if (curChar != 40)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 41:
                  if (curChar != 45)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 43:
                  if (curChar != 38)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 45:
                  if (curChar != 34)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 46:
                  if (curChar != 39)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 48:
                  if ((0xffffdc3affffd8ffL & l) == 0L)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               case 50:
                  if (curChar != 41)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               case 51:
                  if (curChar != 40)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               case 53:
                  if (curChar != 45)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               case 55:
                  if (curChar != 38)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               case 57:
                  if (curChar != 34)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               case 58:
                  if (curChar != 39)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 11:
                  if ((0xefffffffeffffffeL & l) != 0L)
                  {
                     if (kind > 1)
                        kind = 1;
                     jjCheckNAddTwoStates(0, 1);
                  }
                  else if (curChar == 64)
                     jjAddStates(6, 7);
                  else if (curChar == 92)
                     jjAddStates(8, 16);
                  break;
               case 0:
                  if ((0xefffffffeffffffeL & l) == 0L)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 1:
                  if (curChar == 92)
                     jjAddStates(8, 16);
                  break;
               case 4:
                  if (curChar != 64)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 6:
                  if (curChar != 124)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 8:
                  if (curChar != 92)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 13:
                  if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 14:
                  jjAddStates(0, 2);
                  break;
               case 18:
                  if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 17;
                  break;
               case 19:
                  jjAddStates(3, 5);
                  break;
               case 22:
                  if ((0xefffffffeffffffeL & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 23:
                  if (curChar == 92)
                     jjAddStates(17, 25);
                  break;
               case 26:
                  if (curChar != 64)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 28:
                  if (curChar != 124)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 30:
                  if (curChar != 92)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 34:
                  if (curChar == 64)
                     jjAddStates(6, 7);
                  break;
               case 35:
                  if (curChar == 84)
                     jjCheckNAddTwoStates(36, 37);
                  break;
               case 36:
                  if ((0xefffffffeffffffeL & l) == 0L)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 37:
                  if (curChar == 92)
                     jjAddStates(26, 34);
                  break;
               case 40:
                  if (curChar != 64)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 42:
                  if (curChar != 124)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 44:
                  if (curChar != 92)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddTwoStates(36, 37);
                  break;
               case 47:
                  if (curChar == 76)
                     jjCheckNAddTwoStates(48, 49);
                  break;
               case 48:
                  if ((0xefffffffeffffffeL & l) == 0L)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               case 49:
                  if (curChar == 92)
                     jjAddStates(35, 43);
                  break;
               case 52:
                  if (curChar != 64)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               case 54:
                  if (curChar != 124)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               case 56:
                  if (curChar != 92)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAddTwoStates(48, 49);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 11:
               case 0:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAddTwoStates(0, 1);
                  break;
               case 14:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjAddStates(0, 2);
                  break;
               case 19:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjAddStates(3, 5);
                  break;
               case 22:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjAddStates(44, 45);
                  break;
               case 36:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjAddStates(46, 47);
                  break;
               case 48:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjAddStates(48, 49);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 59 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   13, 14, 15, 18, 19, 20, 35, 47, 2, 3, 4, 5, 6, 7, 8, 9, 
   10, 24, 25, 26, 27, 28, 29, 30, 31, 32, 38, 39, 40, 41, 42, 43, 
   44, 45, 46, 50, 51, 52, 53, 54, 55, 56, 57, 58, 22, 23, 36, 37, 
   48, 49, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec2[i2] & l2) != 0L);
      default : 
         if ((jjbitVec0[i1] & l1) != 0L)
            return true;
         return false;
   }
}
public static final String[] jjstrLiteralImages = {
"", null, null, null, "\46", "\174", "\55", null, null, null, null, null, null, 
null, null, null, null, null, null, "\50", "\51", };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
static final long[] jjtoToken = {
   0x1801ffL, 
};
static final long[] jjtoSkip = {
   0x200L, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[59];
private final int[] jjstateSet = new int[118];
protected char curChar;
public TopicValidatorTokenManager(SimpleCharStream stream)
{
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public TopicValidatorTokenManager(SimpleCharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 59; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

}
