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
public class MetaLinkExtractor implements IWebLinkExtractor {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebLinkExtractor#extractLink(com.wxxr.mobile.web.grabber.api.IGrabberServiceContext, org.jsoup.nodes.Element)
	 */
	@Override
	public ExtractedUrlAnchorPair extractLink(IGrabberServiceContext ctx,
			Element elem) {
		Attributes attributes = elem.attributes();

		String equiv = attributes.get("http-equiv");
		String content = attributes.get("content");
		if (equiv != null && content != null) {
			equiv = equiv.toLowerCase();

			// http-equiv="refresh" content="0;URL=http://foo.bar/..."
			if (equiv.equals("refresh")) {
				int pos = content.toLowerCase().indexOf("url=");
				if (pos != -1) {
					String metaRefresh = content.substring(pos + 4);
					ExtractedUrlAnchorPair curUrl = new ExtractedUrlAnchorPair();
					curUrl.setHref(metaRefresh);
					curUrl.setElement(elem);
					curUrl.setAttrName("refresh");
					return curUrl;
				}
			}

			// http-equiv="location" content="http://foo.bar/..."
			if (equiv.equals("location")) {
				String metaLocation = content;
				ExtractedUrlAnchorPair curUrl = new ExtractedUrlAnchorPair();
				curUrl.setHref(metaLocation);
				curUrl.setElement(elem);
				curUrl.setAttrName("location");
				return curUrl;
			}
		}
		return null;
	}

	@Override
	public boolean updateLink(IGrabberServiceContext ctx, ExtractedUrlAnchorPair link,
			String newLink) {
		if("refresh".equals(link.getAttrName())){
			Attributes attributes = link.getElement().attributes();
			String content = attributes.get("content");
			int pos = content.toLowerCase().indexOf("url=");
			content = content.substring(0,pos+4)+newLink;
			link.getElement().attr("content",content);
			return true;
		}else if("location".equals(link.getAttrName())){
			link.getElement().attr("content",newLink);
			return true;
		}
		return false;
	}
	

}
