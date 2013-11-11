/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wxxr.mobile.el;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wxxr.javax.el.ELContext;
import com.wxxr.javax.el.ELProcessor;
import com.wxxr.javax.el.TypeConverter;

/**
 *
 * @author kichung
 */
public class ConvertTest {
    ELProcessor elp;

    public ConvertTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        elp = new ELProcessor();
    }

    @After
    public void tearDown() {
    }

    static public class MyBean {
        String name;
        int pint;
        Integer integer;

        MyBean() {

        }
        MyBean(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public void setPint(int v) {
            this.pint = v;
        }
        public int getPint() {
            return this.pint;
        }

        public void setInteger(Integer i){
            this.integer = i;
        }

        public Integer getInteger() {
            return this.integer;
        }
    }
    @Test
    public void testVoid() {
        MyBean bean = new MyBean();
        elp.defineBean("bean", bean);
        // Assig null to int is 0;
        Object obj = elp.eval("bean.pint = null");
        assertEquals(obj, null);
        assertEquals(bean.getPint(), 0);

        // Assig null to Integer is null
        elp.setValue("bean.integer", null);
        assertEquals(bean.getInteger(), null);
    }

    @Test
    public void testCustom() {
        elp.getELManager().addELResolver(new TypeConverter() {
            @Override
            public Object convertToType(ELContext context, Object obj, Class<?> type) {
                if (obj instanceof String && type == MyBean.class) {
                    context.setPropertyResolved(true);
                    return new MyBean((String) obj);
                }
                return null;
            }
        });
        
        Object val = elp.getValue("'John Doe'", MyBean.class);
        assertTrue(val instanceof MyBean);
        assertEquals(((MyBean)val).getName(), "John Doe");
    }
}