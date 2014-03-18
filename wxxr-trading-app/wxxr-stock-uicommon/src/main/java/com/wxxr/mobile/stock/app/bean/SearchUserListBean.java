/*
 * @(#)SearchUserListBean.java 2014-3-17
 *
 * Copyright 2013-2014 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.stock.app.bean;

import java.util.List;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;
import com.wxxr.mobile.core.ui.common.ListDecorator;

/**
 * 功能描述：
 * 
 * @author maruili
 * @createtime 2014-3-17 下午3:23:47
 */
public class SearchUserListBean implements IBindableBean {
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private List<UserWrapper> searchResult;

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		emitter.addPropertyChangeListener(listener);

	}

	public List<UserWrapper> getSearchResult() {
		return searchResult;
	}

	@Override
	public boolean hasPropertyChangeListener(IPropertyChangeListener listener) {
		return emitter.hasPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		emitter.removePropertyChangeListener(listener);

	}

	/**
	 * @param searchResult
	 *            the searchResult to set
	 */
	public void setSearchResult(List<UserWrapper> searchResult) {
		List<UserWrapper> old = this.searchResult;
		this.searchResult = searchResult;
		if (this.searchResult != null) {
			this.searchResult = new ListDecorator<UserWrapper>("searchResult", this.emitter, this.searchResult);
		}
		this.emitter.firePropertyChange("searchResult", old, this.searchResult);
	}

	@Override
	public String toString() {
		return "SearchUserListBean [" + "searchResult=" + this.searchResult + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((searchResult == null) ? 0 : searchResult.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchUserListBean other = (SearchUserListBean) obj;
		if (searchResult == null) {
			if (other.searchResult != null)
				return false;
		} else if (!searchResult.equals(other.searchResult))
			return false;
		return true;
	}

}
