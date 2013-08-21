/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wxxr.mobile.web.grabber.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.wxxr.mobile.web.grabber.model.ExtractedUrlAnchorPair;

public class HtmlContentHandler {

	private final int MAX_ANCHOR_LENGTH = 100;

	private enum TageName {
		A, AREA, LINK, IFRAME, FRAME, EMBED, IMG, BASE, META, BODY
	}

	private static class HtmlFactory {
		private static Map<String, TageName> name2Element;

		static {
			name2Element = new HashMap<String, TageName>();
			for (TageName element : TageName.values()) {
				name2Element.put(element.toString().toLowerCase(), element);
			}
		}

		public static TageName getTagName(String name) {
			return name2Element.get(name);
		}
	}

	private String base;
	private String metaRefresh;
	private String metaLocation;

//	private boolean isWithinBodyElement;
//	private StringBuilder bodyText;

	private List<ExtractedUrlAnchorPair> outgoingUrls;

	private ExtractedUrlAnchorPair curUrl = null;
//	private boolean anchorFlag = false;
//	private StringBuilder anchorText = new StringBuilder();

	public HtmlContentHandler() {
//		isWithinBodyElement = false;
//		bodyText = new StringBuilder();
		outgoingUrls = new ArrayList<ExtractedUrlAnchorPair>();
	}
	
	public void visit(Element element){
		startElement(element.tagName(),element);
		for (Element child : element.children()) {
			visit(child);
		}
		endElement(element.tagName(),element);
	}

	public void startElement(String localName, Element elem)  {
		
		Attributes attributes = elem.attributes();
		TageName element = HtmlFactory.getTagName(localName);

		if (element == TageName.A || element == TageName.AREA || element == TageName.LINK) {
			String href = attributes.get("href");
			if (href != null) {
//				anchorFlag = true;
				curUrl = new ExtractedUrlAnchorPair();
				curUrl.setHref(href);
				curUrl.setElement(elem);
				curUrl.setAttrName("href");
				outgoingUrls.add(curUrl);
			}
			return;
		}

		if (element == TageName.IMG) {
			String imgSrc = attributes.get("src");
			if (imgSrc != null) {
				curUrl = new ExtractedUrlAnchorPair();
				curUrl.setHref(imgSrc);
				curUrl.setElement(elem);
				curUrl.setAttrName("src");
				outgoingUrls.add(curUrl);
			}
			return;
		}

		if (element == TageName.IFRAME || element == TageName.FRAME || element == TageName.EMBED) {
			String src = attributes.get("src");
			if (src != null) {
				curUrl = new ExtractedUrlAnchorPair();
				curUrl.setHref(src);
				curUrl.setElement(elem);
				curUrl.setAttrName("src");
				outgoingUrls.add(curUrl);
			}
			return;
		}

		if (element == TageName.BASE) {
			if (base != null) { // We only consider the first occurrence of the
								// Base element.
				String href = attributes.get("href");
				if (href != null) {
					base = href;
				}
			}
			return;
		}

		if (element == TageName.META) {
			String equiv = attributes.get("http-equiv");
			String content = attributes.get("content");
			if (equiv != null && content != null) {
				equiv = equiv.toLowerCase();

				// http-equiv="refresh" content="0;URL=http://foo.bar/..."
				if (equiv.equals("refresh") && (metaRefresh == null)) {
					int pos = content.toLowerCase().indexOf("url=");
					if (pos != -1) {
						metaRefresh = content.substring(pos + 4);
					}
					curUrl = new ExtractedUrlAnchorPair();
					curUrl.setHref(metaRefresh);
					outgoingUrls.add(curUrl);
				}

				// http-equiv="location" content="http://foo.bar/..."
				if (equiv.equals("location") && (metaLocation == null)) {
					metaLocation = content;
					curUrl = new ExtractedUrlAnchorPair();
					curUrl.setHref(metaRefresh);
					outgoingUrls.add(curUrl);
				}
			}
			return;
		}
//
//		if (element == TageName.BODY) {
//			isWithinBodyElement = true;
//		}
	}

	public void endElement(String localName,Element elem)  {
		TageName element = HtmlFactory.getTagName(localName);
		if (element == TageName.A || element == TageName.AREA || element == TageName.LINK) {
//			anchorFlag = false;
			String txt = elem.text();
			if ((txt != null)&&(curUrl != null)) {
				String anchor = txt.replaceAll("\n", " ").replaceAll("\t", " ").trim();
				if (!anchor.isEmpty()) {
					if (anchor.length() > MAX_ANCHOR_LENGTH) {
						anchor = anchor.substring(0, MAX_ANCHOR_LENGTH) + "...";
					}
					curUrl.setAnchor(anchor);
				}
			}
			curUrl = null;
		}
	}

	public List<ExtractedUrlAnchorPair> getOutgoingUrls() {
		return outgoingUrls;
	}

	public String getBaseUrl() {
		return base;
	}

}
