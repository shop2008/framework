/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author neillin
 *
 */
public interface IXMLElementReader {
	void readElement(XmlPullParser xpp) throws XmlPullParserException;
}
