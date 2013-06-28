package com.wxxr.mobile.android.ui;

import java.util.Properties;

import junit.framework.TestCase;

public class TagPropertiesParserTest extends TestCase {


	public void testParse() {
		Properties p = TagPropertiesParser.parse("on_click=[handler.invoke(${arg1.value},${arg2.value}],bind=[test ,  , string ],default_value = uiurere 3472473 hjhdjf ");
		assertNotNull(p);
		p.list(System.out);
		assertEquals(3, p.size());
		assertEquals("handler.invoke(${arg1.value},${arg2.value}", p.get("on_click"));
		assertEquals("test ,  , string", p.get("bind"));
		assertEquals("uiurere 3472473 hjhdjf", p.get("default_value"));
	}

}
