/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import static org.xmlpull.v1.XmlPullParser.*;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * @author neillin
 *
 */
public class WorkbenchXML {
	
	public void readXML(InputStream ins,IWorkbenchParserContext ctx) throws XmlPullParserException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(ins, "UTF-8");
        try {
			processDocument(xpp, ctx);
        }catch(XmlPullParserException e){
        	throw e;
		} catch (IOException e) {
			throw new XmlPullParserException("Caught throwable when process workbench xml", xpp, e);
		}
	}
	
	public void processDocument(XmlPullParser xpp,IWorkbenchParserContext ctx)
	        throws XmlPullParserException, IOException
	    {
	        do {
	        	switch(xpp.getEventType()){
	        	case START_DOCUMENT:
	        		ctx.startDocument();
	        		break;
	        	case START_TAG:
	        		IXMLElementReader reader = ctx.getElementReader(xpp.getName());
	        		reader.readElement(xpp);
	        		break;
	        	default:
	        		throw new XmlPullParserException("Unexpecting event",xpp,null);
	        	}
	        } while (xpp.next() != END_DOCUMENT);
	    }
}
