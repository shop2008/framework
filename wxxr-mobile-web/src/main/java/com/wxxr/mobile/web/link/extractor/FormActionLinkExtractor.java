/**
 * 
 */
package com.wxxr.mobile.web.link.extractor;


/**
 * @author neillin
 *
 */
public class FormActionLinkExtractor extends AbstractWebLinkExtractor {

	public FormActionLinkExtractor() {
		super();
		setPrefetchableLink(false);
	}

	@Override
	protected String getLinkAttrName() {
		return "formaction";
	}


}
