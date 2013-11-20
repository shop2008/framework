package com.wxxr.mobile.el;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wxxr.javax.el.ELContext;
import com.wxxr.javax.el.ELManager;
import com.wxxr.javax.el.ELProcessor;
import com.wxxr.javax.el.ExpressionFactory;
import com.wxxr.javax.el.ValueExpression;

public class ValueExpressionTest {

    static ELProcessor elp;
    static ELManager elm;
    static ExpressionFactory factory;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        elp = new ELProcessor();
        elm = elp.getELManager();
        factory = elm.getExpressionFactory();
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testExpr() {
    	elp.defineBean("date", new Date(2001,12,30));
        ValueExpression valExpr = null;
        ELContext ctxt = elm.getELContext();
        	valExpr = factory.createValueExpression(
                ctxt, "#{date.year}", int.class);
        assertTrue(valExpr != null);
        assertEquals(1,valExpr.getReferringBeanNames().size());
        assertEquals("date", valExpr.getReferringBeanNames().get(0));
        assertEquals(1,valExpr.getReferringPropertyNames().size());
        assertEquals("year",valExpr.getReferringPropertyNames().get(0));
        assertTrue(valExpr.isReadOnly(ctxt) == false);
    }

 }

