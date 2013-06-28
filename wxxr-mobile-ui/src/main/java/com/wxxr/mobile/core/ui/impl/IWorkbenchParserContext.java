/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;


/**
 * @author neillin
 *
 */
public interface IWorkbenchParserContext {
	void startDocument();
	void endDocument();
	IXMLElementReader getElementReader(String elemName);
}
