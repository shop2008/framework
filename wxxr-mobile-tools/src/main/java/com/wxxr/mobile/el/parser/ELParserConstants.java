/* Generated By:JJTree&JavaCC: Do not edit this line. ELParserConstants.java */
package com.wxxr.mobile.el.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ELParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int LITERAL_EXPRESSION = 1;
  /** RegularExpression Id. */
  int START_DYNAMIC_EXPRESSION = 2;
  /** RegularExpression Id. */
  int START_DEFERRED_EXPRESSION = 3;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 9;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 10;
  /** RegularExpression Id. */
  int EXPONENT = 11;
  /** RegularExpression Id. */
  int STRING_LITERAL = 12;
  /** RegularExpression Id. */
  int BADLY_ESCAPED_STRING_LITERAL = 13;
  /** RegularExpression Id. */
  int TRUE = 14;
  /** RegularExpression Id. */
  int FALSE = 15;
  /** RegularExpression Id. */
  int NULL = 16;
  /** RegularExpression Id. */
  int DOT = 17;
  /** RegularExpression Id. */
  int AT = 18;
  /** RegularExpression Id. */
  int LBRACE = 19;
  /** RegularExpression Id. */
  int RBRACE = 20;
  /** RegularExpression Id. */
  int PIPE = 21;
  /** RegularExpression Id. */
  int LPAREN = 22;
  /** RegularExpression Id. */
  int RPAREN = 23;
  /** RegularExpression Id. */
  int LBRACK = 24;
  /** RegularExpression Id. */
  int RBRACK = 25;
  /** RegularExpression Id. */
  int COLON = 26;
  /** RegularExpression Id. */
  int COMMA = 27;
  /** RegularExpression Id. */
  int GT0 = 28;
  /** RegularExpression Id. */
  int GT1 = 29;
  /** RegularExpression Id. */
  int LT0 = 30;
  /** RegularExpression Id. */
  int LT1 = 31;
  /** RegularExpression Id. */
  int GE0 = 32;
  /** RegularExpression Id. */
  int GE1 = 33;
  /** RegularExpression Id. */
  int LE0 = 34;
  /** RegularExpression Id. */
  int LE1 = 35;
  /** RegularExpression Id. */
  int EQ0 = 36;
  /** RegularExpression Id. */
  int EQ1 = 37;
  /** RegularExpression Id. */
  int NE0 = 38;
  /** RegularExpression Id. */
  int NE1 = 39;
  /** RegularExpression Id. */
  int NOT0 = 40;
  /** RegularExpression Id. */
  int NOT1 = 41;
  /** RegularExpression Id. */
  int AND0 = 42;
  /** RegularExpression Id. */
  int AND1 = 43;
  /** RegularExpression Id. */
  int OR0 = 44;
  /** RegularExpression Id. */
  int OR1 = 45;
  /** RegularExpression Id. */
  int EMPTY = 46;
  /** RegularExpression Id. */
  int INSTANCEOF = 47;
  /** RegularExpression Id. */
  int MULT = 48;
  /** RegularExpression Id. */
  int PLUS = 49;
  /** RegularExpression Id. */
  int MINUS = 50;
  /** RegularExpression Id. */
  int QUESTIONMARK = 51;
  /** RegularExpression Id. */
  int DIV0 = 52;
  /** RegularExpression Id. */
  int DIV1 = 53;
  /** RegularExpression Id. */
  int MOD0 = 54;
  /** RegularExpression Id. */
  int MOD1 = 55;
  /** RegularExpression Id. */
  int IDENTIFIER = 56;
  /** RegularExpression Id. */
  int NAMESPACE = 57;
  /** RegularExpression Id. */
  int FUNCTIONSUFFIX = 58;
  /** RegularExpression Id. */
  int IMPL_OBJ_START = 59;
  /** RegularExpression Id. */
  int LETTER = 60;
  /** RegularExpression Id. */
  int DIGIT = 61;
  /** RegularExpression Id. */
  int ILLEGAL_CHARACTER = 62;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int IN_EXPRESSION = 1;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "<LITERAL_EXPRESSION>",
    "\"${\"",
    "\"#{\"",
    "\"\\\\\"",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "<INTEGER_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<STRING_LITERAL>",
    "<BADLY_ESCAPED_STRING_LITERAL>",
    "\"true\"",
    "\"false\"",
    "\"null\"",
    "\".\"",
    "\"@\"",
    "\"{\"",
    "\"}\"",
    "\"|\"",
    "\"(\"",
    "\")\"",
    "\"[\"",
    "\"]\"",
    "\":\"",
    "\",\"",
    "\">\"",
    "\"gt\"",
    "\"<\"",
    "\"lt\"",
    "\">=\"",
    "\"ge\"",
    "\"<=\"",
    "\"le\"",
    "\"==\"",
    "\"eq\"",
    "\"!=\"",
    "\"ne\"",
    "\"!\"",
    "\"not\"",
    "\"&&\"",
    "\"and\"",
    "\"||\"",
    "\"or\"",
    "\"empty\"",
    "\"instanceof\"",
    "\"*\"",
    "\"+\"",
    "\"-\"",
    "\"?\"",
    "\"/\"",
    "\"div\"",
    "\"%\"",
    "\"mod\"",
    "<IDENTIFIER>",
    "<NAMESPACE>",
    "<FUNCTIONSUFFIX>",
    "\"#\"",
    "<LETTER>",
    "<DIGIT>",
    "<ILLEGAL_CHARACTER>",
  };

}
