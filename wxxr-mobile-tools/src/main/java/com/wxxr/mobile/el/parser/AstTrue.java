/* Generated By:JJTree: Do not edit this line. AstTrue.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=Ast,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.wxxr.mobile.el.parser;

public
class AstTrue extends SimpleNode {
  public AstTrue(int id) {
    super(id);
  }

  public AstTrue(ELParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ELParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6ae09dbf6870b9858f5ded8591706794 (do not edit this line) */
