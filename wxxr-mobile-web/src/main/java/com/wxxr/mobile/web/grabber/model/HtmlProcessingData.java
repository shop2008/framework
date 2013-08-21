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

package com.wxxr.mobile.web.grabber.model;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;


public class HtmlProcessingData {

	private final Document document;
	private final String currentUrl;
	private String charset;
	
	private List<WebURL> outgoingUrls;
	
	private List<WebURL> downloadedUrls;

	public HtmlProcessingData(Document doc,String url){
		this.document = doc;
		this.currentUrl = url;
	}
	
	public String getHtml() {
		return this.document.outerHtml();
	}


	public String getText() {
		return this.document.body().outerHtml();
	}

	public String getTitle() {
		return this.document.title();
	}

	public List<WebURL> getOutgoingUrls() {
		return outgoingUrls;
	}

	public void setOutgoingUrls(List<WebURL> outgoingUrls) {
		this.outgoingUrls = outgoingUrls;
	}
	
	@Override
	public String toString() {
		return getText();
	}

	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @return the currentUrl
	 */
	public String getCurrentUrl() {
		return currentUrl;
	}

	
	public void addDownloadedUrl(WebURL url){
		if(this.downloadedUrls == null){
			this.downloadedUrls = new ArrayList<WebURL>();
		}
		if(!this.downloadedUrls.contains(url)){
			this.downloadedUrls.add(url);
		}
	}

	/**
	 * @return the downloadedUrls
	 */
	public List<WebURL> getDownloadedUrls() {
		return downloadedUrls;
	}

	/**
	 * @param downloadedUrls the downloadedUrls to set
	 */
	public void setDownloadedUrls(List<WebURL> downloadedUrls) {
		this.downloadedUrls = downloadedUrls;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
}
