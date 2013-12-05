/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.stock.client.binding.ViewPagerAdapterViewFieldBinding.BindingBag;

/**
 * @author dz
 *
 */
public class GenericViewPagerAdapter extends PagerAdapter {

	private final IListDataProvider viewPagerProvider;
	private final IWorkbenchRTContext context;
	
	public GenericViewPagerAdapter(IWorkbenchRTContext ctx, IAndroidBindingContext bCtx, IListDataProvider prov) {
		if((ctx == null)||(bCtx == null)||(prov == null) ) {
			throw new IllegalArgumentException("All arguments cannot be NULL");
		}
		this.viewPagerProvider = prov;
		this.context = ctx;
	}

	public void destroy() {
		for (int i = 0; i < getCount(); i++) {
			View view = (View)viewPagerProvider.getItem(i);

			BindingBag bag = null;
			bag = (BindingBag) view.getTag();
			IBinding<IView> binding = bag.binding;
			binding.deactivate();
			bag.view.doUnbinding(binding);
			context.getWorkbenchManager().getWorkbench().destroyComponent(bag.view);
		}
	}
	public void active() {
		// TODO Auto-generated method stub
		for (int i = 0; i < getCount(); i++) {
			View view = (View)viewPagerProvider.getItem(i);

			BindingBag bag = null;
			bag = (BindingBag) view.getTag();
			IBinding<IView> binding = bag.binding;
			IView vModel = bag.view;
			binding.activate(vModel);
		}
	}

/******************************View Pager Adapter ********************************************/
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = (View)viewPagerProvider.getItem(position);

		container.removeView(view);// 删除页卡
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
		View view = (View)viewPagerProvider.getItem(position);
		container.addView(view, 0);// 添加页卡

		BindingBag bag = null;
		bag = (BindingBag) view.getTag();
		IBinding<IView> binding = bag.binding;
		IView vModel = bag.view;
		binding.activate(vModel);

		return view;
	}

	@Override
	public int getCount() {
		return viewPagerProvider.getItemCounts();// 返回页卡的数量
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		for (int i = 0; i < getCount(); i++) {
			View view = (View)viewPagerProvider.getItem(i);
			
			BindingBag bag = null;
			bag = (BindingBag) view.getTag();
			IBinding<IView> binding = bag.binding;
//			IView vModel = bag.view;
			binding.doUpdate();
		}
		super.notifyDataSetChanged();
	}
}
