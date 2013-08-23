package com.wxxr.mobile.web.grabber.model;

import org.jsoup.nodes.Element;

public class ExtractedUrlAnchorPair {

	private String href;
	private Element element;
	private String attrName;
	private boolean prefetchable;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * @return the attrName
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(Element element) {
		this.element = element;
	}

	/**
	 * @param attrName the attrName to set
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExtractedUrlAnchorPair [href=" + href 
				+ ", attrName=" + attrName + ", element={" + element.outerHtml()+ "} ]";
	}

	/**
	 * @return the prefetchable
	 */
	public boolean isPrefetchable() {
		return prefetchable;
	}

	/**
	 * @param prefetchable the prefetchable to set
	 */
	public void setPrefetchable(boolean prefetchable) {
		this.prefetchable = prefetchable;
	}
	

}
