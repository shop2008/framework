package com.thoughtworks.xstream;

import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.XppDriver;

import junit.framework.TestCase;

public class XStreamTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testToXMLObject() {
		XStream xstream = new XStream();
		xstream.processAnnotations(RendezvousMessage.class);
		RendezvousMessage msg = new RendezvousMessage(15, false,"firstPart","secondPart");
		System.out.println(xstream.toXML(msg));
	}

	public void testFromXMLString() {
		XStream xstream = new XStream();
		xstream.processAnnotations(RendezvousMessage.class);
		Object obj = xstream.fromXML("<message><part>firstPart</part><part>secondPart</part>"+
  "<important>no</important><created>1368944543150</created></message>");
		System.out.println(obj);
	}
	
	public void testToJSONObject() {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.processAnnotations(RendezvousMessage.class);
		RendezvousMessage msg = new RendezvousMessage(15, false,"firstPart","secondPart");
		System.out.println(xstream.toXML(msg));
	}

	public void testFromJSONString() {
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.processAnnotations(RendezvousMessage.class);
		Object obj = xstream.fromXML("{\"message\":{\"part\":[\"firstPart\",\"secondPart\"],\"important\":\"no\",\"created\":1368945787827}}");
		System.out.println(obj);
	}


}
