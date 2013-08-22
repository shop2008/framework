/**
 * 
 */
package com.wxxr.mobile.web.link.extractor;


/**
 * @author neillin
 *
 */
public class CodebaseLinkExtractor extends AbstractWebLinkExtractor {

	@Override
	protected String getLinkAttrName() {
		return "codebase";
	}


}
