/**
 * 
 */
package com.wxxr.mobile.web.link.extractor;


/**
 * @author neillin
 *
 */
public class FormActionLinkExtractor extends AbstractWebLinkExtractor {

	@Override
	protected String getLinkAttrName() {
		return "formaction";
	}


}
