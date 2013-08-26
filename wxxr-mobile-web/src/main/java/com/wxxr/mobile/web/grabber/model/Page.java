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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.wxxr.mobile.core.rpc.http.api.HttpEntity;
import com.wxxr.mobile.core.rpc.http.api.HttpHeaderNames;
import com.wxxr.mobile.core.rpc.http.util.EntityUtils;



/**
 * This class contains the data for a fetched and parsed page.
 *
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class Page implements IWebContent {

    /**
     * The URL of this page.
     */
    protected String url;

    /**
     * The content of this page in binary format.
     */
    protected InputStream contentData;

    /**
     * The ContentType of this page.
     * For example: "text/html; charset=UTF-8"
     */
    protected String contentType;

    /**
     * The encoding of the content.
     * For example: "gzip"
     */
    protected String contentEncoding;

    /**
     * The charset of the content.
     * For example: "UTF-8"
     */
    protected String contentCharset;
    
    protected String lastModifiedDate;
    
    /**
     * Headers which were present in the response of the
     * fetch request
     */
    protected Map<String,String> fetchResponseHeaders;


	public Page(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContent#getWebURL()
	 */
	@Override
	public String getWebURL() {
		return url;
	}

	public void setWebURL(String url) {
		this.url = url;
	}

    /**
     * Loads the content of this page from a fetched
     * HttpEntity.
     */
	public void load(HttpEntity entity) throws Exception {

		contentType = null;
		String type = entity.getContentType();
		if (type != null) {
			contentType = type;
		}

		contentEncoding = null;
		String encoding = entity.getContentEncoding();
		if (encoding != null) {
			contentEncoding = encoding;
		}

		String charset = EntityUtils.getContentCharSet(entity);
		if (charset != null) {
			contentCharset = charset;	
		}
		if("gzip".equalsIgnoreCase(this.contentEncoding)){
			contentData = new GZIPInputStream(entity.getContent());
		}else{
			contentData = entity.getContent();
		}
		this.lastModifiedDate = this.fetchResponseHeaders.get(HttpHeaderNames.LAST_MODIFIED);
	}
	
	/**
     * Returns headers which were present in the response of the
     * fetch request
     */
	public Map<String,String> getFetchResponseHeaders() {
		return fetchResponseHeaders;
	}
	
	public void setFetchResponseHeaders(Map<String,String> headers) {
		fetchResponseHeaders = headers;
	}

    /* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContent#getContentData()
	 */
	@Override
	public InputStream getContentData() {
		return contentData;
	}

	public void setContentData(InputStream contentData) {
		this.contentData = contentData;
	}

    /* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContent#getContentType()
	 */
	@Override
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

    /* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContent#getContentEncoding()
	 */
    @Override
	public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    /* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebContent#getContentCharset()
	 */
	@Override
	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}

	@Override
	public void close() {
		if(this.contentData != null){
			try {
				this.contentData.close();
			} catch (IOException e) {
			}
			this.contentData = null;
		}
	}

	@Override
	public String getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
