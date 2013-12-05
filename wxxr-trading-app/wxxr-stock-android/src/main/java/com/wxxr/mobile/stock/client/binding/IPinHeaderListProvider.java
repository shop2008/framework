package com.wxxr.mobile.stock.client.binding;

import java.util.List;

import com.wxxr.mobile.core.ui.api.IListDataProvider;

public interface IPinHeaderListProvider extends IListDataProvider {
	
	List<Object> getLabels();
	
	List<Object> getLabelPositions();
}
