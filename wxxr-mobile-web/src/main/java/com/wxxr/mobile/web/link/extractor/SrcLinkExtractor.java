/**
 * 
 */
package com.wxxr.mobile.web.link.extractor;


/**
 * @author neillin
 *
 */
public class SrcLinkExtractor extends AbstractWebLinkExtractor {

	@Override
	protected String getLinkAttrName() {
		return "src";
	}


}
