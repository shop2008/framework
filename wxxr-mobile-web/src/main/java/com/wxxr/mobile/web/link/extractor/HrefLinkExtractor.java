/**
 * 
 */
package com.wxxr.mobile.web.link.extractor;


/**
 * @author neillin
 *
 */
public class HrefLinkExtractor extends AbstractWebLinkExtractor {

	@Override
	protected String getLinkAttrName() {
		return "href";
	}


}
