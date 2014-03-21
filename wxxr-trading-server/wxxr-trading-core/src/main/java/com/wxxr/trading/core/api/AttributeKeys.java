/**
 * 
 */
package com.wxxr.trading.core.api;

import com.wxxr.trading.core.model.IAsset;
import com.wxxr.trading.core.model.IAssetAccount;
import com.wxxr.trading.core.model.ITradingRecord;

/**
 * @author neillin
 *
 */
public class AttributeKeys {
	private AttributeKeys(){}
	
	public static final AttributeKey<String> USER_ID_KEY = AttributeKey.createKey("USER_ID", String.class);
	public static final AttributeKey<ITradingRecord> TRADING_RECORD_KEY = AttributeKey.createKey("TRADING_RECORD", ITradingRecord.class);
	public static final AttributeKey<IAsset> FROZEN_ACCT_ASSET_KEY = AttributeKey.createKey("FROZEN_ACCT_ASSET", IAsset.class);
	public static final AttributeKey<Boolean> IS_BUY_STOCK_KEY = AttributeKey.createKey("IS_BUY_STOCK_KEY", Boolean.class);
	public static final AttributeKey<IAssetAccount> ASSET_ACCOUNT_KEY= AttributeKey.createKey("ASSET_ACCOUNT_KEY", IAssetAccount.class);
}
