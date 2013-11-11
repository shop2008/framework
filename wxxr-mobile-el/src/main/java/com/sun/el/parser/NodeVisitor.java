/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.el.parser;


/**
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author$
 */
public interface NodeVisitor {
	  public void visit(SimpleNode node);
	  public void visit(AstAssign node);
	  public void visit(AstConcat node);
	  public void visit(AstDotSuffix node);
	  public void visit(AstLambdaExpression node);
	  public void visit(AstLambdaParameters node);
	  public void visit(AstListData node);
	  public void visit(AstMapData node);
	  public void visit(AstMapEntry node);
	  public void visit(AstMethodArguments node);
	  public void visit(AstCompositeExpression node);
	  public void visit(AstLiteralExpression node);
	  public void visit(AstDeferredExpression node);
	  public void visit(AstDynamicExpression node);
	  public void visit(AstChoice node);
	  public void visit(AstOr node);
	  public void visit(AstAnd node);
	  public void visit(AstEqual node);
	  public void visit(AstNotEqual node);
	  public void visit(AstLessThan node);
	  public void visit(AstGreaterThan node);
	  public void visit(AstLessThanEqual node);
	  public void visit(AstGreaterThanEqual node);
	  public void visit(AstPlus node);
	  public void visit(AstMinus node);
	  public void visit(AstMult node);
	  public void visit(AstDiv node);
	  public void visit(AstMod node);
	  public void visit(AstNegative node);
	  public void visit(AstNot node);
	  public void visit(AstEmpty node);
	  public void visit(AstValue node);
	  public void visit(AstPropertySuffix node);
	  public void visit(AstBracketSuffix node);
	//  public void visit(AstMethodSuffix node);
	//  public void visit(AstClosureSuffix node);
	//  public void visit(AstClosure node);
	  public void visit(AstIdentifier node);
	  public void visit(AstFunction node);
	  public void visit(AstTrue node);
	  public void visit(AstFalse node);
	  public void visit(AstFloatingPoint node);
	  public void visit(AstInteger node);
	  public void visit(AstString node);
	  public void visit(AstNull node);
}
