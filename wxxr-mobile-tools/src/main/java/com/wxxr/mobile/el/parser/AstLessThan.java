/* Generated By:JJTree: Do not edit this line. AstLessThan.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=Ast,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.wxxr.mobile.el.parser;

public
class AstLessThan extends SimpleNode {
  public AstLessThan(int id) {
    super(id);
  }

  public AstLessThan(ELParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ELParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e46d23b4f6e6b2fa60b2aebd5d910bad (do not edit this line) */