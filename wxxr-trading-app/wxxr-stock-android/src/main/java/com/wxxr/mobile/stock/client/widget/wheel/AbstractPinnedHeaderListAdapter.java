/**
 * 
 */
package com.wxxr.mobile.stock.client.widget.wheel;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IRefreshableListAdapter;
import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.android.ui.binding.ListViewPool;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.stock.client.widget.wheel.PinnedHeaderListView.PinnedHeaderAdapter;

/**
 * @author neillin
 *
 */
public abstract class AbstractPinnedHeaderListAdapter extends BaseAdapter implements
		PinnedHeaderAdapter, IRefreshableListAdapter,OnScrollListener {
	private int mLocationPosition = -1;
	private final IListDataProvider dataProvider;
	private final ItemViewSelector viewSelector;
	private final IAndroidBindingContext context;
	private ListViewPool viewPool;
	
	public AbstractPinnedHeaderListAdapter(IAndroidBindingContext ctx,IListDataProvider provider, ItemViewSelector selector){
		if((ctx == null)||(provider == null)||(selector == null)){
			throw new IllegalArgumentException("Binding context, provider, selector cannot be NULL !");
		}
		this.dataProvider = provider;
		this.context = ctx;
		this.viewSelector = selector;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.dataProvider.getItemCounts();
	}

    public Object getItem(int position) {
        return this.dataProvider.getItem(position);
    }
    
    /**
     * Returns the item ID for the specified list position.
     */
    public long getItemId(int position) {
       return position;
    }

    /**
     * Returns false if any partition has a header.
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    
    protected boolean isHeaderData(Object data){
    	return false;
    }
    /**
     * Returns true for all items except headers.
     */
    @Override
    public boolean isEnabled(int position) {
        return isHeaderData(getItem(position)) == false;
    }

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getViewPool().getView(null, convertView, getItem(position), position);
	}

	/**
	 * 
	 */
	protected ListViewPool getViewPool() {
		if(this.viewPool == null){
			this.viewPool = new ListViewPool(context, viewSelector);
		}
		return this.viewPool;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IRefreshableListAdapter#destroy()
	 */
	@Override
	public void destroy() {
		if(this.viewPool != null){
			this.viewPool.destroy();
			this.viewPool = null;
		}

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IRefreshableListAdapter#refresh()
	 */
	@Override
	public boolean refresh() {
		return this.dataProvider.updateDataIfNeccessary();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
	}
	
	public View getHeaderView() {
		return getViewPool().createUI(getHeaderViewId());
	}
	
	protected abstract String getHeaderViewId();
	
	protected int getPosition4NextSection(int position){
		int size = this.dataProvider.getItemCounts();
		for(int i=position;i < size ; i++){
			Object data = this.dataProvider.getItem(i);
			if(isHeaderData(data)){
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0
				|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int nextSectionPosition = getPosition4NextSection(position);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		getViewPool();
		this.viewPool.updateView(header,getItem(position),position);
	}


}
