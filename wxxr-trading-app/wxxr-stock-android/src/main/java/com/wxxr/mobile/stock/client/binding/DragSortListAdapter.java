package com.wxxr.mobile.stock.client.binding;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IRefreshableListAdapter;
import com.wxxr.mobile.android.ui.binding.CascadeAndroidBindingCtx;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.stock.client.binding.ViewPagerAdapterViewFieldBinding.BindingBag;

public class DragSortListAdapter extends ArrayAdapter<Object>  implements IRefreshableListAdapter {
	private final IListDataProvider dataProvider;
	private final IAndroidBindingContext context;
	private final String itemViewId;
	
	public DragSortListAdapter(IAndroidBindingContext ctx,IListDataProvider provider,String itemViewId){
		super(ctx.getUIContext(), 0);
		if((ctx == null)||(provider == null)){
			throw new IllegalArgumentException("Binding context, provider, cannot be NULL !");
		}
		this.context = ctx;
		this.dataProvider = provider;
		this.itemViewId = itemViewId;
	}

	@Override
	public boolean refresh() {
		boolean result =  this.dataProvider.updateDataIfNeccessary();
		notifyDataSetChanged();
		return result;
	}
	
	@Override
	public int getCount() {
		return this.dataProvider.getItemCounts();
	}
	@Override
	public Object getItem(int position) {
		return this.dataProvider.getItem(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IViewDescriptor v = this.context.getWorkbenchManager().getViewDescriptor(itemViewId);
		IBindingDescriptor bDesc = v.getBindingDescriptor(TargetUISystem.ANDROID);
		IBinding<IView> binding = null;
		IViewBinder vBinder = this.context.getWorkbenchManager().getViewBinder();
		CascadeAndroidBindingCtx ctx = new CascadeAndroidBindingCtx(context);
		binding = vBinder.createBinding(ctx, bDesc);
		View view = (View)binding.getUIControl();
		BindingBag bag = new BindingBag();
		bag.binding = binding;
		bag.ctx = ctx;
		bag.view = this.context.getWorkbenchManager().getWorkbench().createNInitializedView(itemViewId);
		view.setTag(bag);
		IView vModel = bag.view;
		vModel.getAdaptor(IModelUpdater.class).updateModel(getItem(position));
		binding.activate(vModel);
		binding.doUpdate();
		return view;
	}

	@Override
	public void destroy() {
		
	}
}
