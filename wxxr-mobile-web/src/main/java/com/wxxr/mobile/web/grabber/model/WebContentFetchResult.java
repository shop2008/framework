package com.wxxr.mobile.web.grabber.model;

import java.util.Map;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.rpc.http.api.HttpEntity;
import com.wxxr.mobile.core.rpc.http.util.EntityUtils;


/**
 * @author neillin
 *
 */
public class WebContentFetchResult {

	protected static final Trace logger = Trace.getLogger(WebContentFetchResult.class);

	protected int statusCode;
	protected HttpEntity entity = null;
	protected Map<String, String> responseHeaders = null;
	protected String fetchedUrl = null;
	protected String movedToUrl = null;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public HttpEntity getEntity() {
		return entity;
	}

	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}
	
	public Map<String, String> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Map<String, String> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	
	public String getFetchedUrl() {
		return fetchedUrl;
	}

	public void setFetchedUrl(String fetchedUrl) {
		this.fetchedUrl = fetchedUrl;
	}

	public boolean fetchContent(Page page) {
		try {
			page.load(entity);
			page.setFetchResponseHeaders(responseHeaders);
			return true;
		} catch (Exception e) {
			logger.info("Exception while fetching content for: " + page.getWebURL() + " [" + e.getMessage()
					+ "]");
		}
		return false;
	}

	public void discardContentIfNotConsumed() {
		try {
			if (entity != null) {
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			if(logger.isTraceEnabled()){
				logger.trace("Caught exception when discardContentIfNotConsumed", e);
			}
		}
	}

	public String getMovedToUrl() {
		return movedToUrl;
	}

	public void setMovedToUrl(String movedToUrl) {
		this.movedToUrl = movedToUrl;
	}

}
