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
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.wxxr.mobile.web.grabber.api.IGrabberServiceContext;
import com.wxxr.mobile.web.grabber.api.IWebLinkExractorRegistry;
import com.wxxr.mobile.web.grabber.api.IWebLinkExtractor;
import com.wxxr.mobile.web.grabber.model.ExtractedUrlAnchorPair;

public class HtmlContentHandler {


	private String base;

	private List<ExtractedUrlAnchorPair> outgoingUrls;

	private final IGrabberServiceContext context;

	public HtmlContentHandler(IGrabberServiceContext ctx) {
		outgoingUrls = new ArrayList<ExtractedUrlAnchorPair>();
		this.context = ctx;
	}
	
	public void visit(Element element){
		startElement(element.tagName(),element);
		for (Element child : element.children()) {
			visit(child);
		}
		endElement(element.tagName().toLowerCase(),element);
	}

	public void startElement(String localName, Element elem)  {
		
		if ("base".equals(localName)) {
			Attributes attributes = elem.attributes();
			if (base != null) { // We only consider the first occurrence of the
								// Base element.
				String href = attributes.get("href");
				if (href != null) {
					base = href;
				}
			}
			return;
		}
		IWebLinkExractorRegistry registry = this.context.getService(IWebLinkExractorRegistry.class);
		IWebLinkExtractor[] extractors = registry.getLinkExtractors(localName);
		if(extractors != null){
			for (IWebLinkExtractor extractor : extractors) {
				ExtractedUrlAnchorPair link = extractor.extractLink(context, elem);
				if(link != null){
					this.outgoingUrls.add(link);
				}
			}
		}
	}

	public void endElement(String localName,Element elem)  {
	}

	public List<ExtractedUrlAnchorPair> getOutgoingUrls() {
		return outgoingUrls;
	}

	public String getBaseUrl() {
		return base;
	}

}
