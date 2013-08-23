/**
 * 
 */
package com.wxxr.mobile.web.link.extractor;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.wxxr.mobile.web.grabber.api.IGrabberServiceContext;
import com.wxxr.mobile.web.grabber.api.IWebLinkExtractor;
import com.wxxr.mobile.web.grabber.model.ExtractedUrlAnchorPair;

/**
 * @author neillin
 *
 */
public abstract class AbstractWebLinkExtractor implements IWebLinkExtractor {

	private boolean prefetchableLink = true;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebLinkExtractor#extractLink(com.wxxr.mobile.web.grabber.api.IGrabberServiceContext, org.jsoup.nodes.Element)
	 */
	@Override
	public ExtractedUrlAnchorPair extractLink(IGrabberServiceContext ctx,
			Element elem) {
		Attributes attributes = elem.attributes();
		String linkAttr = getLinkAttrName();
			String href = attributes.get(linkAttr);
			if (href != null) {
				ExtractedUrlAnchorPair curUrl = new ExtractedUrlAnchorPair();
				curUrl.setHref(href);
				curUrl.setElement(elem);
				curUrl.setAttrName(linkAttr);
				curUrl.setPrefetchable(this.prefetchableLink);
				return curUrl;
			}
		return null;
	}
	
	protected abstract String getLinkAttrName();

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebLinkExtractor#updateLink(com.wxxr.mobile.web.grabber.api.IGrabberServiceContext, org.jsoup.nodes.Element, java.lang.String)
	 */
	@Override
	public boolean updateLink(IGrabberServiceContext ctx, ExtractedUrlAnchorPair link,
			String newLink) {
		if(getLinkAttrName().equals(link.getAttrName())){
			link.getElement().attr(getLinkAttrName(), newLink);
			return true;
		}
		return false;
	}

	/**
	 * @return the prefetchableLink
	 */
	public boolean isPrefetchableLink() {
		return prefetchableLink;
	}

	/**
	 * @param prefetchableLink the prefetchableLink to set
	 */
	public void setPrefetchableLink(boolean prefetchableLink) {
		this.prefetchableLink = prefetchableLink;
	}

}
