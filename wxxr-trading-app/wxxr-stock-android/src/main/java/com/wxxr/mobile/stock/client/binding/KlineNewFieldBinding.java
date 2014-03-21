/**
 * 
 */
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
import com.wxxr.mobile.stock.client.widget.KlineViewNew;

/**
 *
 */
public class KlineNewFieldBinding extends BasicFieldBinding implements IObservableListDataProvider{
	private IListDataProvider listAdapter;
	private IDataChangedListener listener;
	
	public KlineNewFieldBinding(IAndroidBindingContext ctx, String fieldName,
			Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
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
			((KlineViewNew)getUIControl()).setDataProvider(this);
		}
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

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		if(this.listAdapter != null){
			((KlineViewNew)getUIControl()).setDataProvider(null);
			this.listAdapter = null;
		}
		super.deactivate();
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IListDataProvider#getItem(int)
	 */
	public Object getItem(int arg0) {
		return listAdapter.getItem(arg0);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IListDataProvider#getItemCounts()
	 */
	public int getItemCounts() {
		return listAdapter.getItemCounts();
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IListDataProvider#getItemId(java.lang.Object)
	 */
	public Object getItemId(Object arg0) {
		return listAdapter.getItemId(arg0);
	}

	@Override
	public void registerDataChangedListener(IDataChangedListener l) {
		this.listener = l;
	}

	@Override
	public boolean unregisterDataChangedListener(IDataChangedListener l) {
		if(this.listener == l){
			this.listener = null;
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#updateUI(boolean)
	 */
	@Override
	protected void updateUI(boolean recursive) {
		if(listAdapter != null)
			listAdapter.updateDataIfNeccessary();
		if(this.listener != null){
			this.listener.dataSetChanged();
		}
		super.updateUI(recursive);
	}

	@Override
	public boolean isItemEnabled(Object arg0) {
		return listAdapter.isItemEnabled(arg0);
	}

	@Override
	public boolean updateDataIfNeccessary() {
		return this.listAdapter.updateDataIfNeccessary();
	}

	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		// TODO Auto-generated method stub
		super.notifyDataChanged(events);
	}

}
