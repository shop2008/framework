/**
 * 
 */
package com.wxxr.trading.core.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;

import com.hygensoft.common.util.Trace;
import com.wxxr.common.util.CommonUtils;
import com.wxxr.trading.core.model.IAssetAccount;

/**
 * @author neillin
 *
 */
public class TradingContext implements Synchronization{
	private static final Trace log = Trace.register(TradingContext.class);
	private static ThreadLocal<Stack<TradingContext>> local = new ThreadLocal<Stack<TradingContext>>() {

		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected Stack<TradingContext> initialValue() {
			return new Stack<TradingContext>();
		}
		
	};
	
	public static void push(TradingContext ctx){
		local.get().push(ctx);
	}
	
	public static TradingContext pop(){
		Stack<TradingContext> stack = local.get();
		if(stack.isEmpty()){
			return null;
		}
		return stack.pop();
	}
	
	public static TradingContext current() {
		Stack<TradingContext> stack = local.get();
		if(stack.isEmpty()){
			return null;
		}
		return stack.peek();
	}
	
	public static TradingContext createNewContext(Transaction tx) {
		return new TradingContext(tx);
	}
	
	private LinkedList<Long> lockedAccountIds = new LinkedList<Long>();
	private Map<AttributeKey<?>, Object> attrs = new HashMap<AttributeKey<?>, Object>();
	private final Transaction tx;
	private boolean inclearing = false;
	
	private TradingContext(Transaction tx) {
		this.tx = tx;
		try {
			if(log.isDebugEnabled()){
				log.debug("A new TradingContext was created, tx :"+tx+", tx status :"+(tx != null ? getTxStatusName(tx.getStatus()) : "N/A"));
			}
			if((tx != null)&&(tx.getStatus() == Status.STATUS_ACTIVE)){
				tx.registerSynchronization(this);
			}
		}catch(Throwable t){
			log.error("Failed to attach TrandingContext to transaction :"+tx, t);
			tx = null;
		}
	}
	
	public static String getTxStatusName(int status) {
		switch(status){
		case Status.STATUS_ACTIVE:
				return "ACTIVE";
		case Status.STATUS_COMMITTED:
			return "COMMITTED";
		case Status.STATUS_COMMITTING:
			return "COMMITTING";
		case Status.STATUS_MARKED_ROLLBACK:
			return "MARKED_ROLLBACK";
		case Status.STATUS_NO_TRANSACTION:
			return "NO_TRANSACTION";
		case Status.STATUS_PREPARED:
			return "PREPARED";
		case Status.STATUS_PREPARING:
			return "PREPARING";
		case Status.STATUS_ROLLEDBACK:
			return "ROLLEDBACK";
		case Status.STATUS_ROLLING_BACK:
			return "ROLLING_BACK";
		default:
			return "UNKNOWN";
		}
	}

	public Transaction getCurrentTransaction() {
		return this.tx;
	}
	
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return getAttribute(AttributeKeys.USER_ID_KEY);
	}

	/**
	 * @return the tradingAccount
	 */
	public IAssetAccount getAssetAccount() {
		return getAttribute(AttributeKeys.ASSET_ACCOUNT_KEY);
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		setAttribute(AttributeKeys.USER_ID_KEY, userId);
	}

	/**
	 * @param assetAccount the tradingAccount to set
	 */
	public void setAssetAccount(IAssetAccount assetAccount) {
		setAttribute(AttributeKeys.ASSET_ACCOUNT_KEY, assetAccount);
	}
	
	public <T> T getAttribute(AttributeKey<T> key){
		return key.cast(this.attrs.get(key));
	}
	
	public <T> void setAttribute(AttributeKey<T> key , T val){
		this.attrs.put(key, val);
	}
	
	public void addLockAccountId(Long acctId) {
		if(log.isDebugEnabled()){
			log.debug("Account lock :"+acctId+" was registered !");
		}

		if(!this.lockedAccountIds.contains(acctId)){
			this.lockedAccountIds.add(acctId);
		}
	}
	
	public boolean removeLockAccountId(Long acctId) {
		if(log.isDebugEnabled()){
			log.debug("Remove account lock :"+acctId);
		}
		return this.lockedAccountIds.remove(acctId);
	}

	
	public boolean isAccountLocked(Long acctId) {
		return this.lockedAccountIds.contains(acctId);
	}
	
	public List<Long> getLockedAccountIds() {
		return Collections.unmodifiableList(this.lockedAccountIds);
	}
	
	public void destroy() {
		// if there is not transaction, unlock all locked accounts now. otherwise defer unlocking to transaction completed
		if(log.isDebugEnabled()){
			log.debug("Context was detroy!");
		}
		if(this.tx == null){
			if(log.isDebugEnabled()){
				log.debug("There is not transaction associated with this context, context will be cleared now !");
			}
			if(log.isDebugEnabled()){
				log.debug("destroy0");
			}
			unlockAccounts();
			this.attrs.clear();
			this.lockedAccountIds.clear();
			if(log.isDebugEnabled()){
				log.debug("destroy1");
			}
		}else{
			if(log.isDebugEnabled()){
				log.debug("There is a transaction associated with this context, context clearance will be perform when transaction completed!");
			}
		}
	}

	@Override
	public void afterCompletion(int state) {
		if(log.isDebugEnabled()){
			log.debug("Transaction completed, context will be cleared now !");
		}
		if(log.isDebugEnabled()){
			log.debug("afterCompletion0");
		}
		unlockAccounts();		
		this.attrs.clear();
		this.lockedAccountIds.clear();
		if(log.isDebugEnabled()){
			log.debug("afterCompletion1");
		}
	}

	public boolean inClearProcess(){
		return this.inclearing;
	}
	
	public void setInClearProcessing(boolean bool){
		this.inclearing = bool;
	}
	/**
	 * 
	 */
	protected void unlockAccounts() {
		if(!this.lockedAccountIds.isEmpty()){
			IGenericAccountManager mgr = CommonUtils.getService(IGenericAccountManager.class);
			Long[] ids = this.lockedAccountIds.toArray(new Long[this.lockedAccountIds.size()]);
			for (Long acctId : ids) {
				if(log.isDebugEnabled()){
					log.debug("Going to unlock account : "+acctId);
				}
				mgr.unlockAccount(acctId);
			}
		}
	}

	@Override
	public void beforeCompletion() {
		
	}

}
