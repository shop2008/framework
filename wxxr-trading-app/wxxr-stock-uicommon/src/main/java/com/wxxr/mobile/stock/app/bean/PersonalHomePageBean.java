/*
 * Generated code, don't modified !
 */
package com.wxxr.mobile.stock.app.bean;

import java.util.List;
import com.wxxr.mobile.core.ui.common.ListDecorator;
import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

/**
 * Generated by Bindable Bean generator
 *
 */
public class PersonalHomePageBean implements IBindableBean {
	
	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private List<GainBean> virtualList;
	private Long voucherVol;
	private List<GainBean> actualList;
	private double totalProfit;
	private long virtualCount;
	private long actualCount;

	/**
	 * @param listener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		emitter.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		emitter.removePropertyChangeListener(listener);
	}



	/**
	 * @return the virtualList
	 */
	public List<GainBean> getVirtualList() {
		return virtualList;
	}

	/**
	 * @param virtualList the virtualList to set
	 */
	public void setVirtualList(List<GainBean> virtualList) {
		List<GainBean> old = this.virtualList;
		this.virtualList = virtualList;
		if(this.virtualList != null){
            this.virtualList = new ListDecorator<GainBean>("virtualList", this.emitter,this.virtualList);
        }
		this.emitter.firePropertyChange("virtualList", old, this.virtualList);
	}

	/**
	 * @return the voucherVol
	 */
	public Long getVoucherVol() {
		return voucherVol;
	}

	/**
	 * @param voucherVol the voucherVol to set
	 */
	public void setVoucherVol(Long voucherVol) {
		Long old = this.voucherVol;
		this.voucherVol = voucherVol;
		this.emitter.firePropertyChange("voucherVol", old, this.voucherVol);
	}

	/**
	 * @return the actualList
	 */
	public List<GainBean> getActualList() {
		return actualList;
	}

	/**
	 * @param actualList the actualList to set
	 */
	public void setActualList(List<GainBean> actualList) {
		List<GainBean> old = this.actualList;
		this.actualList = actualList;
		if(this.actualList != null){
            this.actualList = new ListDecorator<GainBean>("actualList", this.emitter,this.actualList);
        }
		this.emitter.firePropertyChange("actualList", old, this.actualList);
	}

	/**
	 * @return the totalProfit
	 */
	public double getTotalProfit() {
		return totalProfit;
	}

	/**
	 * @param totalProfit the totalProfit to set
	 */
	public void setTotalProfit(double totalProfit) {
		double old = this.totalProfit;
		this.totalProfit = totalProfit;
		this.emitter.firePropertyChange("totalProfit", old, this.totalProfit);
	}

	/**
	 * @return the virtualCount
	 */
	public long getVirtualCount() {
		return virtualCount;
	}

	/**
	 * @param virtualCount the virtualCount to set
	 */
	public void setVirtualCount(long virtualCount) {
		long old = this.virtualCount;
		this.virtualCount = virtualCount;
		this.emitter.firePropertyChange("virtualCount", old, this.virtualCount);
	}

	/**
	 * @return the actualCount
	 */
	public long getActualCount() {
		return actualCount;
	}

	/**
	 * @param actualCount the actualCount to set
	 */
	public void setActualCount(long actualCount) {
		long old = this.actualCount;
		this.actualCount = actualCount;
		this.emitter.firePropertyChange("actualCount", old, this.actualCount);
	}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override   
    public String toString() {
        return "PersonalHomePageBean ["+
                "virtualList=" + this.virtualList +
                " , voucherVol=" + this.voucherVol +
                " , actualList=" + this.actualList +
                " , totalProfit=" + this.totalProfit +
                " , virtualCount=" + this.virtualCount +
                " , actualCount=" + this.actualCount +
        "]";
    }	

}
