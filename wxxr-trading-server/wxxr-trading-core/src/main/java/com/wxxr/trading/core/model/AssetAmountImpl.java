/**
 * 
 */
package com.wxxr.trading.core.model;

/**
 * @author neillin
 *
 */
public abstract class AssetAmountImpl implements IAssetAmount {

	private long amount;
	private boolean virtual;
	
	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.account.model.AssetAmount#getAmount()
	 */
	@Override
	public long getAmount() {
		return this.amount;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.account.model.AssetAmount#isVirtual()
	 */
	@Override
	public boolean isVirtual() {
		return this.virtual;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}

	/**
	 * @param virtual the virtual to set
	 */
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	
	

}
