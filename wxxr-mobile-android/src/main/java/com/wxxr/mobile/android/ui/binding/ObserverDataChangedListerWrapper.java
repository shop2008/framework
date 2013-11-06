/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import android.database.DataSetObserver;

import com.wxxr.mobile.core.ui.api.IDataChangedListener;

/**
 * @author neillin
 *
 */
public class ObserverDataChangedListerWrapper implements IDataChangedListener {
	private final DataSetObserver observer;
	
	public ObserverDataChangedListerWrapper(DataSetObserver observer){
		this.observer = observer;
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IDataChangedListener#dataSetChanged()
	 */
	@Override
	public void dataSetChanged() {
		this.observer.onInvalidated();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IDataChangedListener#dataItemChanged()
	 */
	@Override
	public void dataItemChanged() {
		this.observer.onChanged();
	}

	/**
	 * @return the observer
	 */
	public DataSetObserver getObserver() {
		return observer;
	}

}
