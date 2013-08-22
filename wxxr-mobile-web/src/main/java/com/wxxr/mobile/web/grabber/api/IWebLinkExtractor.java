/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;

import org.jsoup.nodes.Element;

import com.wxxr.mobile.web.grabber.model.ExtractedUrlAnchorPair;

/**
 * @author neillin
 *
 */
public interface IWebLinkExtractor {
	ExtractedUrlAnchorPair extractLink(IGrabberServiceContext ctx,Element element);
	boolean updateLink(IGrabberServiceContext ctx,ExtractedUrlAnchorPair link, String newLink);
}
