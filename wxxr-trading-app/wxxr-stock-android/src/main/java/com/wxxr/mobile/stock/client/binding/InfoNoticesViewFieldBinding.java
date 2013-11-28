package com.wxxr.mobile.stock.client.binding;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.stock.client.widget.InfoNoticesView;

public class InfoNoticesViewFieldBinding extends BasicFieldBinding implements IObservableListDataProvider{

	private IListDataProvider listAdapter;
	private IDataChangedListener listener;
	public InfoNoticesViewFieldBinding(IAndroidBindingContext ctx, String fieldName,
			Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		
		super.notifyDataChanged(events);
	}

	@Override
	public void activate(IView model) {
		super.activate(model);
		IUIComponent comp = model.getChild(getFieldName());
		listAdapter = comp.getAdaptor(IListDataProvider.class);
		
		if (listAdapter == null) {
			listAdapter = createAdaptorFromValue(comp);
		}
		if(listAdapter != null){
			listAdapter.updateDataIfNeccessary();
			((InfoNoticesView)getUIControl()).setDataProvider(this);
		}
	}
	
	
	@Override
	protected void updateUI(boolean recursive) {
		if (this.listAdapter != null) {
			this.listAdapter.updateDataIfNeccessary();
		}
		if(this.listener != null){
			this.listener.dataSetChanged();
		}
		super.updateUI(recursive);
	}

	@Override
	public void deactivate() {
		if(this.listAdapter != null){
			((InfoNoticesView)getUIControl()).setDataProvider(null);
			this.listAdapter = null;
		}
		super.deactivate();
	}
	
	/**
	 * @param provider
	 * @param val
	 * @return
	 */
	protected IListDataProvider createAdaptorFromValue(final IUIComponent comp) {
		return new IListDataProvider() {
			Object[]  data = null;
			@Override
			public Object getItemId(Object item) {
				return null;
			}

			@Override
			public int getItemCounts() {
				return data != null ? data.length : 0;
			}

			@Override
			public Object getItem(int i) {
				return data[i];
			}

			@Override
			public boolean isItemEnabled(Object item) {
				return true;
			}

			@Override
			public boolean updateDataIfNeccessary() {
				data = getListData(comp);
				return true;
			}
		};
	}
	
	protected Object[] getListData(IUIComponent comp){
		if(comp.hasAttribute(AttributeKeys.options)){
			List<Object> result = comp.getAttribute(AttributeKeys.options);
			return result != null ? result.toArray() : null;
		}
		if (comp instanceof IDataField) {
			Object val = ((IDataField<?>) comp).getValue();
			if (val instanceof List){
				return ((List<Object>)val).toArray();
			}else if((val != null)&&val.getClass().isArray()){
				return (Object[])val;
			}
		}
		return null;
	}

	
	@Override
	public int getItemCounts() {
		
		return listAdapter.getItemCounts();
	}

	@Override
	public Object getItem(int i) {
		return listAdapter.getItem(i);
	}

	@Override
	public Object getItemId(Object item) {
		return listAdapter.getItemId(item);
	}

	@Override
	public boolean isItemEnabled(Object item) {
		return listAdapter.isItemEnabled(item);
	}

	@Override
	public boolean updateDataIfNeccessary() {
		return this.listAdapter.updateDataIfNeccessary();
	}

	@Override
	public void registerDataChangedListener(IDataChangedListener listener) {
	 	this.listener = listener;
	}

	@Override
	public boolean unregisterDataChangedListener(IDataChangedListener listener) {
		if(this.listener == listener){
			this.listener = null;
			return true;
		}
		return false;
	}

}
