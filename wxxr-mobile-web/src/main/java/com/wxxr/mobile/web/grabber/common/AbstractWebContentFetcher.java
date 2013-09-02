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

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.params.HttpProtocolParams;
import org.apache.log4j.Logger;

import com.wxxr.mobile.core.rpc.http.api.HttpEntity;
import com.wxxr.mobile.core.rpc.http.api.HttpHeaderNames;
import com.wxxr.mobile.core.rpc.http.api.HttpMethod;
import com.wxxr.mobile.core.rpc.http.api.HttpRequest;
import com.wxxr.mobile.core.rpc.http.api.HttpResponse;
import com.wxxr.mobile.core.rpc.http.api.HttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.HttpStatus;
import com.wxxr.mobile.core.rpc.http.api.ParamConstants;
import com.wxxr.mobile.web.grabber.api.IWebContentFetcher;
import com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask;
import com.wxxr.mobile.web.grabber.model.URLCanonicalizer;
import com.wxxr.mobile.web.grabber.model.WebContentFetchResult;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public abstract class AbstractWebContentFetcher implements IWebContentFetcher {

	protected static final Logger logger = Logger.getLogger(AbstractWebContentFetcher.class);


	protected final Object mutex = new Object();

	protected long lastFetchTime = 0;
	private int httpRequestTimeout = 60,politenessDelay = 0;

	protected HttpRpcService getHttpService() {
		return getService(HttpRpcService.class);
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.crawler4j.fetcher.IPageFetcher#fetchHeader(edu.uci.ics.crawler4j.url.WebURL)
	 */
	@Override
	public WebContentFetchResult fetchHeader(IWebPageGrabbingTask task,WebURL webUrl,String lastModified) {
		WebContentFetchResult fetchResult = new WebContentFetchResult();
		HttpRequest request = null;
		String toFetchURL = webUrl.getURL();
		try {
//			String toFetchURL = URLEncoder.encode(webUrl.getURL(),"UTF-8");
			synchronized (mutex) {
				long now = (new Date()).getTime();
				if (now - lastFetchTime < this.politenessDelay) {
					Thread.sleep(politenessDelay - (now - lastFetchTime));
				}
				lastFetchTime = (new Date()).getTime();
			}
			Map<String, Object> params = new HashMap<String, Object>();
//			params.put(HttpHeaderNames.ACCEPT_ENCODING, "gzip");
			if(lastModified != null){
				params.put(HttpHeaderNames.IF_MODIFIED_SINCE,lastModified);
			}
			String userAgent = (String)task.getContext().getAttribute(HttpHeaderNames.USER_AGENT);
			if(userAgent != null){
				params.put(HttpHeaderNames.USER_AGENT, userAgent);
			}

			params.put(ParamConstants.PARAMETER_KEY_HTTP_METHOD, HttpMethod.GET);
			request = getHttpService().createRequest(toFetchURL, params);
			HttpResponse response = request.invoke(this.httpRequestTimeout, TimeUnit.SECONDS);
			
			int statusCode = response.getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				if (statusCode != HttpStatus.SC_NOT_FOUND) {
					if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
						String header = response.getHeader("Location");
						if (header != null) {
							String movedToUrl = header;
							movedToUrl = URLCanonicalizer.getCanonicalURL(movedToUrl, toFetchURL);
							fetchResult.setMovedToUrl(movedToUrl);
						} 
						fetchResult.setStatusCode(statusCode);
						return fetchResult;
					}
					logger.info("Failed: " + response.getStatus().toString() + ", while fetching " + toFetchURL);
				}
				fetchResult.setStatusCode(response.getStatusCode());
				return fetchResult;
			}
			fetchResult.setEntity((HttpEntity)response.getResponseEntity());
			fetchResult.setResponseHeaders(response.getHeaders());
			fetchResult.setFetchedUrl(toFetchURL);
			String uri = request.getURI();
			if (!uri.equals(toFetchURL)) {
				if (!URLCanonicalizer.getCanonicalURL(uri).equals(toFetchURL)) {
					fetchResult.setFetchedUrl(uri);
				}
			}

			if (fetchResult.getEntity() != null) {
				long size = fetchResult.getEntity().getContentLength();
				if (size == -1) {
					String length = response.getHeader("Content-Length");
					if (length == null) {
						length = response.getHeader("Content-length");
					}
					if (length != null) {
						size = Integer.parseInt(length);
					} else {
						size = -1;
					}
				}
				if (size > task.getMaxDownloadSize()) {
					fetchResult.setStatusCode(CustomFetchStatus.PageTooBig);
					request.abort();
					return fetchResult;
				}

				fetchResult.setStatusCode(HttpStatus.SC_OK);
				return fetchResult;

			}
			
			request.abort();
			
		} catch (IOException e) {
			logger.error("Fatal transport error: " + e.getMessage() + " while fetching " + toFetchURL
					+ " (link found in doc #" + webUrl.getParentUrl() + ")");
			fetchResult.setStatusCode(CustomFetchStatus.FatalTransportError);
			return fetchResult;
		} catch (IllegalStateException e) {
			// ignoring exceptions that occur because of not registering https
			// and other schemes
		} catch (Exception e) {
				logger.error("Error while fetching " + webUrl.getURL(),e);
		} finally {
			try {
				if (fetchResult.getEntity() == null && request != null) {
					request.abort();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fetchResult.setStatusCode(CustomFetchStatus.UnknownError);
		return fetchResult;
	}


	/**
	 * @return the lastFetchTime
	 */
	public Date getLastFetchTime() {
		return lastFetchTime > 0 ? new Date(lastFetchTime) : null;
	}

	/**
	 * @return the httpRequestTimeout
	 */
	public int getHttpRequestTimeout() {
		return httpRequestTimeout;
	}

	/**
	 * @return the politenessDelay
	 */
	public int getPolitenessDelay() {
		return politenessDelay;
	}


	/**
	 * @param httpRequestTimeout the httpRequestTimeout to set
	 */
	public void setHttpRequestTimeout(int httpRequestTimeout) {
		this.httpRequestTimeout = httpRequestTimeout;
	}

	/**
	 * @param politenessDelay the politenessDelay to set
	 */
	public void setPolitenessDelay(int politenessDelay) {
		this.politenessDelay = politenessDelay;
	}

	protected abstract <T> T getService(Class<T> clazz);

}
