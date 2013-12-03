/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.ViewPagerAdapterViewFieldBinding.BindingBag;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewPagerDataProvider;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author dz
 *
 */
public class GenericViewPagerAdapter extends PagerAdapter {

	private final IViewPagerDataProvider viewPagerProvider;
	
	public GenericViewPagerAdapter(IWorkbenchRTContext ctx, IAndroidBindingContext bCtx, IViewPagerDataProvider prov) {
		if((ctx == null)||(bCtx == null)||(prov == null) ) {
			throw new IllegalArgumentException("All arguments cannot be NULL");
		}
		this.viewPagerProvider = prov;
	}

	public void destroy() {
		for (int i = 0; i < getCount(); i++) {
			View view = (View)viewPagerProvider.getViewItem(i);

			BindingBag bag = null;
			bag = (BindingBag) view.getTag();
			IBinding<IView> binding = bag.binding;
			binding.deactivate();
		}
	}
	public void active() {
		// TODO Auto-generated method stub
		for (int i = 0; i < getCount(); i++) {
			View view = (View)viewPagerProvider.getViewItem(i);
//			IView v = (IView) viewPagerProvider.getItem(i);
//			View view = createUI(v.getName());

			BindingBag bag = null;
			bag = (BindingBag) view.getTag();

			IBinding<IView> binding = bag.binding;
			IView vModel = bag.view;
			IModelUpdater updater = vModel.getAdaptor(IModelUpdater.class);
			if(updater != null)
				updater.updateModel(viewPagerProvider.getAttributeData());
			binding.activate(vModel);
		}
	}

/******************************View Pager Adapter ********************************************/
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = (View)viewPagerProvider.getViewItem(position);
//		IView v = (IView) viewPagerProvider.getItem(position);
//		View view = createUI(v.getName());

		container.removeView(view);// 删除页卡
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
		View view = (View)viewPagerProvider.getViewItem(position);
		
//		IView v = (IView) viewPagerProvider.getItem(position);
//		View view = createUI(v.getName());
		container.addView(view, 0);// 添加页卡

		BindingBag bag = null;
		bag = (BindingBag) view.getTag();

		IBinding<IView> binding = bag.binding;
		IView vModel = bag.view;
		IModelUpdater updater = vModel.getAdaptor(IModelUpdater.class);
		if (updater != null)
			updater.updateModel(viewPagerProvider.getAttributeData());
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
			View view = (View)viewPagerProvider.getViewItem(i);
//			IView v = (IView) viewPagerProvider.getItem(i);
//			View view = createUI(v.getName());

			BindingBag bag = null;
			bag = (BindingBag) view.getTag();

			IBinding<IView> binding = bag.binding;
			IView vModel = bag.view;
			IModelUpdater updater = vModel.getAdaptor(IModelUpdater.class);
			if (updater != null)
				updater.updateModel(viewPagerProvider.getAttributeData());
			binding.refresh();
			// binding.activate(vModel);
		}
		super.notifyDataSetChanged();
	}
	
}
