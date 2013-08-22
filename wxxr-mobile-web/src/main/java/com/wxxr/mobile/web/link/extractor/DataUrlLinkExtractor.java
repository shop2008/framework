/**
 * 
 */
package com.wxxr.mobile.web.link.extractor;


/**
 * @author neillin
 *
 */
public class DataUrlLinkExtractor extends AbstractWebLinkExtractor {

	@Override
	protected String getLinkAttrName() {
		return "data-url";
	}


}
