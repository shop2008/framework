/**
 * 
 */
package com.wxxr.trading.core.common;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.wxxr.common.microkernel.IKernelContext;
import com.wxxr.trading.core.api.ITradingCodeBuilder;
import com.wxxr.trading.core.api.ITradingCodeManager;
import com.wxxr.trading.core.api.ITradingStrategy;
import com.wxxr.trading.core.model.ITrading;
import com.wxxr.trading.core.model.ITradingCode;

/**
 * @author wangyan
 *
 */
public abstract class TradingCodeManager implements ITradingCodeManager {

	private Map<String,ITradingStrategy<ITrading>> strategies=new ConcurrentHashMap<String,ITradingStrategy<ITrading>>();


	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingCodeManager#registerStrategy(java.lang.String, com.wxxr.trading.core.api.ITradingStrategy)
	 */
	@Override
	public void registerStrategy(String tradingCode,
			ITradingStrategy<ITrading> strategy) {
		if(strategies.get(tradingCode)!=null){
			throw new IllegalArgumentException("Register strategy failed. Trading code "+tradingCode+" already exist.");
		}
		strategies.put(tradingCode, strategy);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingCodeManager#unregisterStrategy(java.lang.String, com.wxxr.trading.core.api.ITradingStrategy)
	 */
	@Override
	public void unregisterStrategy(String tradingCode,
			ITradingStrategy<ITrading> strategy) {
		ITradingStrategy<? extends ITrading> old=strategies.get(tradingCode);
		if(old==strategy){
			strategies.remove(tradingCode);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingCodeManager#getTradingCode(java.lang.String)
	 */
	@Override
	public ITradingCode getTradingCode(String tradingCode) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingCodeManager#getTradingCode(java.lang.Integer)
	 */
	@Override
	public ITradingCode getTradingCode(Integer tradingCodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.ITradingCodeManager#getStrategy(java.lang.String)
	 */
	@Override
	public ITradingStrategy<ITrading> getStrategy(String tradingCode) {
		return strategies.get(tradingCode);
	}

	public void start(IKernelContext context) {
		context.registerService(ITradingCodeManager.class, this);
	}

	public void stop(IKernelContext context) {
		context.unregisterService(ITradingCodeManager.class, this);
	}

	public ITradingCode buildITradingCode(ITrading trading){
		return getTradingCodeBuilder(trading.getType()).build(trading);
	}
	protected ITradingCodeBuilder getTradingCodeBuilder(String type){
		ServiceLoader<ITradingCodeBuilder> serviceLoader=ServiceLoader.load(ITradingCodeBuilder.class);
		for(ITradingCodeBuilder builder :serviceLoader){
			if(builder.getSupportType().equals(type)){
				return builder;
			}
		}
		return null;
	}
}
