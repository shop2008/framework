/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IRefreshableListAdapter;
import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.android.ui.binding.ListViewPool;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.stock.client.widget.PinnedHeaderListView;
import com.wxxr.mobile.stock.client.widget.PinnedHeaderListView.PinnedHeaderAdapter;

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
	private int unItemPosition = 0;
	private int titlePos = 0;
	private int firstVisibleItemPos = 0;
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
		Object val = getItem(position);
		String vid = this.viewSelector.getItemViewId(val);
		return getViewPool().getView(vid, convertView, getItem(position), position);
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
		boolean result =  this.dataProvider.updateDataIfNeccessary();
		notifyDataSetChanged();
		return result;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
			firstVisibleItemPos = firstVisibleItem;
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
	}
	
	public int getFirstVisibleItem() {
		return firstVisibleItemPos;
	}
	
	public View getPinnedHeaderView() {
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

	
	public List<Integer> getTitleSection() {
	//	int count = 0;
		List<Integer> positions = null;
		if(this.dataProvider != null) {
			positions = new ArrayList<Integer>();
			for(int i=0;i<dataProvider.getItemCounts();i++) {
				Object obj = this.dataProvider.getItem(i);
				if(isHeaderData(obj)) {
					positions.add(i);
					//count ++;
				}
			}
		}
		return positions;
	}
	
	
	public void setUnItemPosition(int position) {
		this.unItemPosition = position;
	}
	
	public int getUnItemPosition() {
		return this.unItemPosition;
	}
	
	protected int getPosition4PreviousSection(int position){
		for(int i=position-1;i>=0; i--){
			Object data = this.dataProvider.getItem(i);
			if(isHeaderData(data)){
				return i;
			}
		}
		return 0;
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
		if (position >= titlePos) {
			this.viewPool.updateView(header,getItem(position),position);
		} else {
			this.viewPool.updateView(header,getItem(getPosition4PreviousSection(position)),position);
		}
		titlePos = position;
	}

	
	
	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#getItemViewType(int)
	 */
	@Override
	public int getItemViewType(int position) {
		if(this.viewSelector != null){
			Object val = getItem(position);
			String vid = this.viewSelector.getItemViewId(val);
			String[] ids = this.viewSelector.getAllViewIds();
			for (int i = 0; i < ids.length; i++) {
				if(ids[i].equals(vid)){
					return i;
				}			
			}
		}
		return super.getItemViewType(position);
	}
	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#getViewTypeCount()
	 */
	@Override
	public int getViewTypeCount() {
		if(this.viewSelector != null){
			return this.viewSelector.getAllViewIds().length;
		}
		return super.getViewTypeCount();
	}	

}
