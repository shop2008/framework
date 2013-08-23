package com.wxxr.mobile.web.grabber.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.web.grabber.api.IHTMLParser;
import com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask;
import com.wxxr.mobile.web.grabber.model.ExtractedUrlAnchorPair;
import com.wxxr.mobile.web.grabber.model.HtmlProcessingData;
import com.wxxr.mobile.web.grabber.model.IWebContent;
import com.wxxr.mobile.web.grabber.model.URLCanonicalizer;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * neillin
 */
public class HtmlParserImpl implements IHTMLParser {

	protected static final Trace logger = Trace.getLogger(HtmlParserImpl.class.getName());

	
	/* (non-Javadoc)
	 * @see edu.uci.ics.crawler4j.parser.IWebParser#parse(edu.uci.ics.crawler4j.crawler.Page, java.lang.String)
	 */
	@Override
	public HtmlProcessingData parse(IWebPageGrabbingTask task,IWebContent page, String contextURL) throws IOException {

		HtmlProcessingData parseData = null;
		HtmlContentHandler contentHandler = new HtmlContentHandler(task.getContext());
		Document dom = Jsoup.parse(page.getContentData(),page.getContentCharset(),page.getWebURL());
		contentHandler.visit(dom);
		parseData = new HtmlProcessingData(dom,page.getWebURL());

		List<WebURL> outgoingUrls = new ArrayList<WebURL>();

		String baseURL = contentHandler.getBaseUrl();
		if (baseURL != null) {
			contextURL = baseURL;
		}

		int urlCount = 0;
		for (ExtractedUrlAnchorPair urlAnchorPair : contentHandler.getOutgoingUrls()) {
			String href = urlAnchorPair.getHref();
			href = href.trim();
			if (href.length() == 0) {
				continue;
			}
			String hrefWithoutProtocol = href.toLowerCase();
			if (href.startsWith("http://")) {
				hrefWithoutProtocol = href.substring(7);
			}
			if (!hrefWithoutProtocol.contains("javascript:") && !hrefWithoutProtocol.contains("mailto:")
					&& !hrefWithoutProtocol.contains("@")) {
				String url = URLCanonicalizer.getCanonicalURL(href, contextURL);
				if (url != null) {
					WebURL webURL = new WebURL();
					webURL.setURL(url);
					webURL.setAnchor(urlAnchorPair);
					outgoingUrls.add(webURL);
					urlCount++;
					if(logger.isDebugEnabled()){
						logger.debug("Found outgoing link"+urlCount+" :"+webURL);
					}
					if (urlCount > task.getMaxOutgoingLinksToFollow()) {
						break;
					}
				}
			}
		}
		parseData.setCharset(page.getContentCharset());
		parseData.setOutgoingUrls(outgoingUrls);
		return parseData;

	}
}
