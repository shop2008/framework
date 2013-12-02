package com.wxxr.mobile.stock.client.binding;

import java.util.List;
import java.util.Map;

import android.graphics.Color;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.stock.client.widget.MinuteLineView;
import com.wxxr.mobile.stock.client.widget.MinuteLineViewKeys;

public class MinuteLineViewFieldBinding extends BasicFieldBinding implements IObservableListDataProvider{

	private IListDataProvider listAdapter;
	private IDataChangedListener listener;
	public MinuteLineViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
		
	}

	@Override
	public void activate(IView model) {
		IUIComponent comp = model.getChild(getFieldName());
		listAdapter = comp.getAdaptor(IListDataProvider.class);
		if (listAdapter == null) {
			listAdapter = createAdaptorFromValue(comp);
		}
		if(listAdapter != null){
			((MinuteLineView)getUIControl()).setDataProvider(this);
		}
		super.activate(model);
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
	public void deactivate() {
		if(this.listAdapter != null){
			((MinuteLineView)getUIControl()).setDataProvider(null);
			this.listAdapter = null;
		}
		super.deactivate();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void updateModel() {
		super.updateModel();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#updateUI(boolean)
	 */
	@Override
	protected void updateUI(boolean arg0) {
		this.listAdapter.updateDataIfNeccessary();
		if(this.listener != null){
			this.listener.dataSetChanged();
		}
		IUIComponent comp = getField();
		MinuteLineView view = (MinuteLineView)getUIControl();
		String stockClose = comp.getAttribute(MinuteLineViewKeys.stockClose);
		if(stockClose!=null){
			view.setStockClose(stockClose);
		}
		
		String stockType = comp.getAttribute(MinuteLineViewKeys.stockType);
		if(stockType!=null){
			view.setStockType(stockType);
		}
		String border = comp.getAttribute(MinuteLineViewKeys.stockBorderColor);
		if(border != null){
			view.setBorderColor(getColor(border));
		}
		
		String up = comp.getAttribute(MinuteLineViewKeys.stockUpColor);
		if(up!=null){
			view.setStockUpColor(getColor(up));
		}
		
		String down = comp.getAttribute(MinuteLineViewKeys.stockDownColor);
		if(down!=null){
			view.setStockDownColor(getColor(down));
		}
		
		String close = comp.getAttribute(MinuteLineViewKeys.stockCloseColor);
		if(close!=null){
			view.setStockCloseColor(getColor(close));
		}
		
		String average = comp.getAttribute(MinuteLineViewKeys.stockAverageLineColor);
		if(average!=null){
			view.setStockAverageLineColor(getColor(average));
		}
		super.updateUI(arg0);
	}
	
	private int getColor(String val){
		int color = 0;
		if(RUtils.isResourceIdURI(val)){
			color = AppUtils.getFramework().getAndroidApplication().getResources().getColor(RUtils.getInstance().getResourceIdByURI(val)); 
		}else{
			color =Color.parseColor(val);
		}
		return color;
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
		return listAdapter.updateDataIfNeccessary();
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
