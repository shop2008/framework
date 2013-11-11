/**
 * 
 */
package com.sun.el.parser;

/**
 * @author neillin
 *
 */
public class AbstractNodeVisitor implements NodeVisitor {

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.SimpleNode)
	 */
	@Override
	public void visit(SimpleNode node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstAssign)
	 */
	@Override
	public void visit(AstAssign node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstConcat)
	 */
	@Override
	public void visit(AstConcat node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstDotSuffix)
	 */
	@Override
	public void visit(AstDotSuffix node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstLambdaExpression)
	 */
	@Override
	public void visit(AstLambdaExpression node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstLambdaParameters)
	 */
	@Override
	public void visit(AstLambdaParameters node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstListData)
	 */
	@Override
	public void visit(AstListData node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstMapData)
	 */
	@Override
	public void visit(AstMapData node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstMapEntry)
	 */
	@Override
	public void visit(AstMapEntry node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstMethodArguments)
	 */
	@Override
	public void visit(AstMethodArguments node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstCompositeExpression)
	 */
	@Override
	public void visit(AstCompositeExpression node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstLiteralExpression)
	 */
	@Override
	public void visit(AstLiteralExpression node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstDeferredExpression)
	 */
	@Override
	public void visit(AstDeferredExpression node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstDynamicExpression)
	 */
	@Override
	public void visit(AstDynamicExpression node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstChoice)
	 */
	@Override
	public void visit(AstChoice node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstOr)
	 */
	@Override
	public void visit(AstOr node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstAnd)
	 */
	@Override
	public void visit(AstAnd node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstEqual)
	 */
	@Override
	public void visit(AstEqual node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstNotEqual)
	 */
	@Override
	public void visit(AstNotEqual node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstLessThan)
	 */
	@Override
	public void visit(AstLessThan node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstGreaterThan)
	 */
	@Override
	public void visit(AstGreaterThan node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstLessThanEqual)
	 */
	@Override
	public void visit(AstLessThanEqual node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstGreaterThanEqual)
	 */
	@Override
	public void visit(AstGreaterThanEqual node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstPlus)
	 */
	@Override
	public void visit(AstPlus node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstMinus)
	 */
	@Override
	public void visit(AstMinus node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstMult)
	 */
	@Override
	public void visit(AstMult node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstDiv)
	 */
	@Override
	public void visit(AstDiv node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstMod)
	 */
	@Override
	public void visit(AstMod node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstNegative)
	 */
	@Override
	public void visit(AstNegative node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstNot)
	 */
	@Override
	public void visit(AstNot node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstEmpty)
	 */
	@Override
	public void visit(AstEmpty node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstValue)
	 */
	@Override
	public void visit(AstValue node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstPropertySuffix)
	 */
	@Override
	public void visit(AstPropertySuffix node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstBracketSuffix)
	 */
	@Override
	public void visit(AstBracketSuffix node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstIdentifier)
	 */
	@Override
	public void visit(AstIdentifier node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstFunction)
	 */
	@Override
	public void visit(AstFunction node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstTrue)
	 */
	@Override
	public void visit(AstTrue node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstFalse)
	 */
	@Override
	public void visit(AstFalse node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstFloatingPoint)
	 */
	@Override
	public void visit(AstFloatingPoint node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstInteger)
	 */
	@Override
	public void visit(AstInteger node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstString)
	 */
	@Override
	public void visit(AstString node) {
		node.visitChildren(this);
	}

	/* (non-Javadoc)
	 * @see com.sun.el.parser.NodeVisitor#visit(com.sun.el.parser.AstNull)
	 */
	@Override
	public void visit(AstNull node) {
		node.visitChildren(this);
	}

}
