/**
 * 
 */
package com.wxxr.mobile.web.link.extractor;


/**
 * @author neillin
 *
 */
public class ActionLinkExtractor extends AbstractWebLinkExtractor {

	public ActionLinkExtractor() {
		super();
		setPrefetchableLink(false);
	}

	@Override
	protected String getLinkAttrName() {
		return "action";
	}


}
