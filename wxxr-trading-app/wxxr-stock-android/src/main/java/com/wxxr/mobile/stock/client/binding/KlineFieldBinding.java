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
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.stock.client.widget.KLineView;

/**
 * @author neillin
 *
 */
public class KlineFieldBinding extends BasicFieldBinding implements IObservableListDataProvider{
	public static final String LIST_ITEM_VIEW_ID = "itemViewId";
	public static final String LIST_FOOTER_VIEW_ID = "footerViewId";
	public static final String LIST_HEADER_VIEW_ID = "headererViewId";
	private IListDataProvider listAdapter;
	private IDataChangedListener listener;
	
	public KlineFieldBinding(IAndroidBindingContext ctx, String fieldName,
			Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#activate(com.wxxr.mobile.core.ui.api.IUIComponent)
	 */
	@Override
	public void activate(IView model) {
		super.activate(model);
		IUIComponent comp = model.getChild(getFieldName());
		IListDataProvider provider = comp.getAdaptor(IListDataProvider.class);
		if(provider == null){
			if(comp instanceof IDataField){
				Object val = ((IDataField<?>)comp).getValue();
				provider = createAdaptorFromValue(val);
			}
		}
		if(provider == null){
			provider = createAdaptorFromValue(comp.getAttribute(AttributeKeys.options));
		}
		if(provider != null){
			this.listAdapter = provider;
			((KLineView)getUIControl()).setDataProvider(this);
		}
	}

	/**
	 * @param provider
	 * @param val
	 * @return
	 */
	protected IListDataProvider createAdaptorFromValue(Object val) {
		IListDataProvider provider = null;
		if(val instanceof List){
			final List data = (List)val;
			provider = new IListDataProvider() {
				
				@Override
				public Object getItemId(Object item) {
					return null;
				}
				
				@Override
				public int getItemCounts() {
					return data.size();
				}
				
				@Override
				public Object getItem(int i) {
					return data.get(i);
				}

				@Override
				public boolean isItemEnabled(Object arg0) {
					return true;
				}
			};
		}else if((val != null)&& val.getClass().isArray()){
			final Object[] data = (Object[])val;
			provider = new IListDataProvider() {
				
				@Override
				public Object getItemId(Object item) {
					return null;
				}
				
				@Override
				public int getItemCounts() {
					return data.length;
				}
				
				@Override
				public Object getItem(int i) {
					return data[i];
				}

				@Override
				public boolean isItemEnabled(Object arg0) {
					return true;
				}
			};
		}
		return provider;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		if(this.listAdapter != null){
			((KLineView)getUIControl()).setDataProvider(null);
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
		if(this.listener != null){
			this.listener.dataSetChanged();
		}
		super.updateUI(recursive);
	}

	@Override
	public boolean isItemEnabled(Object arg0) {
		return listAdapter.isItemEnabled(arg0);
	}

}
