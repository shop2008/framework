package com.wxxr.mobile.web.grabber.model;

import java.io.InputStream;


public interface IWebContent {

	String getWebURL();

	/**
	 * Returns the content of this page in binary format.
	 */
	InputStream getContentData();

	/**
	 * Returns the ContentType of this page.
	 * For example: "text/html; charset=UTF-8"
	 */
	String getContentType();

	/**
	 * Returns the encoding of the content.
	 * For example: "gzip"
	 */
	String getContentEncoding();

	/**
	 * Returns the charset of the content.
	 * For example: "UTF-8"
	 */
	String getContentCharset();
	
	void close();
	
	/**
	 * get last modified date of content in HTTP-date format : "EEE, dd MMM yyyy HH:mm:ss zzz"
	 * @return
	 */
	String getLastModifiedDate();

}